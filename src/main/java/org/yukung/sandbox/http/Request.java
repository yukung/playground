package org.yukung.sandbox.http;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yukung
 */
class Request {

    private static final String LF = System.getProperty("line.separator");
    private static final int MAX_HEADER_SIZE = 16000;
    private static final int FILE_BUFFER_SIZE = 8000;
    private static final Pattern COMMAND = Pattern.compile("^(\\w+)\\s+(.+?)\\s+HTTP/([\\d.]+)$");
    private static final Pattern LINEAR_WHITE_SPACE = Pattern.compile("\\r\\n[ \\t]+");
    private static final Pattern CONTENT_LENGTH = Pattern.compile("^Content-Length:[ \\t]*(\\d+)", Pattern.CASE_INSENSITIVE);

    private SocketChannel channel;
    private String method;
    private String version;
    private String path;
    private String[] metadata;
    private int contentLength;
    private int lastPosition;
    private ByteBuffer buffer;
    private ByteBuffer headerBuffer;
    private ByteBuffer fileBuffer;
    private FileChannel file;
    private HttpServer server;
    private boolean startToWrite;

    Request(SocketChannel initChannel, HttpServer initServer) {
        server = initServer;
        channel = initChannel;
        buffer = ByteBuffer.allocate(MAX_HEADER_SIZE);
        lastPosition = 0;
    }

    void readyToRead(SelectionKey key) throws IOException {
        if (metadata == null) {
            try {
                header(key);
            } catch (BadRequestException e) {
                if (server.isDebugEnabled()) {
                    e.printStackTrace();
                }
                response(key, e);
            }
        } else {
            content(key);
        }
    }

    void readyToWrite(SelectionKey key) throws IOException {
        if (headerBuffer != null) {
            setWriteOp(key);
            channel.write(headerBuffer);
            if (headerBuffer.remaining() > 0) {
                return;
            }
            if (fileBuffer == null) {
                close(key);
            }
            headerBuffer = null;
            return;
        }
        if (fileBuffer != null) {
            if (fileBuffer.remaining() > 0) {
                channel.write(fileBuffer);
            }
            while (fileBuffer.remaining() == 0) {
                if (!prepareBuffer()) {
                    close(key);
                    return;
                }
                channel.write(fileBuffer);
            }
        }
    }

    private void setWriteOp(SelectionKey key) {
        if (startToWrite || !key.isValid()) return;
        key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        startToWrite = true;
    }

    void abort(SelectionKey key) {
        try {
            close(key);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (file != null) {
                try {
                    file.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean prepareBuffer() throws IOException {
        fileBuffer.clear();
        if (file.read(fileBuffer) < 0) {
            file.close();
            file = null;
            fileBuffer = null;
        } else {
            fileBuffer.flip();
        }
        return fileBuffer != null && fileBuffer.remaining() > 0;
    }

    private void close(SelectionKey key) throws IOException {
        key.cancel();
        channel.close();
    }

    private void header(SelectionKey key) throws IOException {
        int len = channel.read(buffer);
        if (len < 0) {
            throw new BadRequestException("header too short: " + new String(buffer.array(), 0, buffer.position()));
        } else if (len == 0) {
            return;
        }
        for (int i = buffer.position() - 1; i > lastPosition; i--) {
            if (buffer.get(i) == '\n' && buffer.get(i - 1) == '\r' && buffer.get(i - 2) == '\n' && buffer.get(i - 3) == '\r') {
                createHeader(buffer.array(), i - 4);
                buffer.limit(buffer.position());
                buffer.position(i + 1);
                buffer.compact();
                content(key);
                return;
            }
        }
        if (buffer.remaining() == 0) {
            throw new BadRequestException("header too long: " + new String(buffer.array(), 0, buffer.position()), "header too long", HttpURLConnection.HTTP_BAD_REQUEST);
        }
        lastPosition = buffer.position();
        if (lastPosition < 3) {
            lastPosition = 3;
        } else {
            lastPosition -= 3;
        }
    }

    private void content(SelectionKey key) throws IOException {
        if (contentLength > 0) {
            if (buffer.position() == 0) {
                channel.read(buffer);
            }
            if (buffer.position() > 0) {
                do {
                    contentLength -= buffer.position();
                    buffer.clear();
                    channel.read(buffer);
                } while (buffer.position() > 0);
            }
            if (contentLength > 0) {
                return;
            }
        }
        responseToClient(key);
    }

    private void responseToClient(SelectionKey key) throws IOException {
        if (path.equals("/quit")) {
            response(key, 200, "OK", true);
            server.close();
        } else {
            response(key);
        }
    }

    private void response(SelectionKey key) throws IOException {
        if (method.equals("GET") || method.equals("POST")) {
            File f = new File(".", path);
            if (!f.getAbsolutePath().contains("..") && f.isFile()) {
                response(key, f);
            } else {
                if (server.isDebugEnabled()) {
                    System.out.println(f.getAbsolutePath() + " is not found.");
                }
                throw new BadRequestException(path + " not found", HttpURLConnection.HTTP_NOT_FOUND);
            }
        } else {
            throw new BadRequestException("unknown method: " + method, HttpURLConnection.HTTP_BAD_METHOD);
        }
    }

    private void response(SelectionKey key, File f) throws IOException {
        file = new FileInputStream(f).getChannel();
        fileBuffer = ByteBuffer.allocate(FILE_BUFFER_SIZE);
        fileBuffer.limit(0);
        responseSuccess(key, (int) f.length(), "text/html");
    }

    private void responseSuccess(SelectionKey key, int length, String type) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter(baos);
        pw.print("HTTP/1.1 200 OK\r\n");
        pw.print("Connection: close\r\n");
        pw.print("Content-Length: ");
        pw.print(length);
        pw.print("\r\n");
        pw.print("Content-Type: ");
        pw.print(type);
        pw.print("\r\n\r\n");
        pw.flush();
        response(key, baos.toByteArray(), false);
    }

    private void response(SelectionKey key, BadRequestException e) throws IOException {
        response(key, e.statusCode, e.responseMessage, false);
    }

    private void response(SelectionKey key, int status, String msg, boolean sync) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter(baos);
        pw.print("HTTP/1.1 ");
        pw.print(status);
        pw.print(" ");
        pw.print(msg);
        pw.print("\r\n");
        server.authority.challenge(pw);
        pw.print("\r\n\r\n");
        pw.flush();
        response(key, baos.toByteArray(), sync);
    }

    private void response(SelectionKey key, byte[] buffer, boolean sync) throws IOException {
        if (sync) {
            key.cancel();
            channel.configureBlocking(true);
        }
        headerBuffer = ByteBuffer.wrap(buffer);
        readyToWrite(key);
    }

    private void createHeader(byte[] buff, int len) {
        for (int i = 0; i < len; i++) {
            if (i > 2 && buff[i - 1] == '\r' && buff[i] == '\n') {
                Matcher m = COMMAND.matcher(new String(buff, 0, i - 1));
                if (m.matches()) {
                    method = m.group(1);
                    path = m.group(2);
                    version = m.group(3);
                } else {
                    throw new BadRequestException(new String(buff, 0, i + 1), "header too long",
                            HttpURLConnection.HTTP_BAD_REQUEST);
                }
                String headers = LINEAR_WHITE_SPACE.matcher(new String(buff, i + 1, len - i)).replaceAll(" ");
                metadata = headers.split("\\r\\n");
                contentLength = 0;
                for (String field : metadata) {
                    Matcher mn = CONTENT_LENGTH.matcher(field);
                    if (mn.matches()) {
                        contentLength = Integer.parseInt(mn.group(1));
                    }
                }
                break;
            }
        }
        server.authority.authorize(metadata);

        if (server.isDebugEnabled()) {
            System.out.println(this);
            for (String field : metadata) {
                System.out.println(field);
            }
        }
    }

    public String toString() {
        return super.toString() + LF + method + ' ' + path + " HTTP/" + version;
    }

    static class BadRequestException extends RuntimeException {
        String responseMessage;
        int statusCode;

        BadRequestException(String msg, String resp, int initCode) {
            super(msg);
            responseMessage = resp;
            statusCode = initCode;
        }

        BadRequestException(String msg, int initCode) {
            super(msg);
            responseMessage = msg;
            statusCode = initCode;
        }

        BadRequestException(String msg) {
            this(msg, HttpURLConnection.HTTP_BAD_REQUEST);
        }
    }
}
