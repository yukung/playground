package org.yukung.sandbox.http;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yukung
 */
public class HttpServer {

    static final int DEFAULT_PORT = 8800;
    private static final Pattern COMMAND = Pattern.compile("^(\\w+)\\s+(.+?)\\s+HTTP/([\\d.]+)$");
    private static final String LF = System.getProperty("line.separator");
    private static final int MAX_HEADER_SIZE = 16384;
    private ServerSocket serverSocket;
    private boolean debug;

    HttpServer(int port) throws IOException {
        serverSocket = new ServerSocket();
        serverSocket.setReuseAddress(true);
        serverSocket.bind(new InetSocketAddress(port));
    }

    public static void main(String[] args) throws IOException {
        int port = DEFAULT_PORT;
        boolean debug = false;
        for (String arg : args) {
            if (arg.equals("-d")) {
                debug = true;
            } else if (arg.matches("^\\d+$")) {
                port = Integer.parseInt(arg);
            }
        }
        HttpServer server = new HttpServer(port);
        server.setDebug(debug);
        server.service();
        server.close();
    }

    void close() {
        if (serverSocket == null) {
            return;
        }
        try {
            serverSocket.close();
            serverSocket = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void service() throws IOException {
        assert serverSocket != null;

        for (Socket sock = accept(); sock != null; sock = accept()) {
            try {
                Request req = new Request(sock);
                if (req.path.equals("/quit")) {
                    response(200, "OK", sock.getOutputStream());
                    break;
                } else {
                    response(req, sock.getOutputStream());
                }
            } catch (BadRequestException e) {
                if (debug) {
                    e.printStackTrace();
                }
                response(sock.getOutputStream(), e);
            } finally {
                sock.close();
            }
        }
    }

    private void response(OutputStream out, BadRequestException e) {
        response(e.statusCode, e.responseMessage, out);
    }

    private void response(Request req, OutputStream out) throws IOException {
        if (req.method.equals("GET")) {
            File f = new File(".", req.path);
            if (!f.getAbsolutePath().contains("..") && f.isFile()) {
                response(f, out);
            } else {
                if (debug) {
                    System.out.println(f.getAbsolutePath() + " is not found.");
                }
                throw new BadRequestException(req.path + " not found", HttpURLConnection.HTTP_NOT_FOUND);
            }
        } else {
            throw new BadRequestException("unknown method: " + req.method, HttpURLConnection.HTTP_BAD_METHOD);
        }
    }

    private void response(File f, OutputStream out) throws IOException {
        responseSuccess((int) f.length(), "text/html", out);
        try (BufferedInputStream buf = new BufferedInputStream(new FileInputStream(f))) {
            for (int c = buf.read(); c >= 0; c = buf.read()) {
                out.write(c);
            }
        }
    }

    private void responseSuccess(int len, String type, OutputStream out) {
        PrintWriter pw = new PrintWriter(out);
        pw.print("HTTP/1.1 200 OK\r\n");
        pw.print("Connection: close\r\n");
        pw.print("Content-Length: ");
        pw.print(len);
        pw.print("\r\n");
        pw.print("Content-Type: ");
        pw.print(type);
        pw.print("\r\n\r\n");
        pw.flush();
    }

    private void response(int status, String msg, OutputStream out) {
        PrintWriter pw = new PrintWriter(out);
        pw.print("HTTP/1.1 ");
        pw.print(status);
        pw.print(" ");
        pw.print(msg);
        pw.print("\r\n\r\n");
        pw.flush();
    }

    private Socket accept() throws IOException {
        try {
            return serverSocket.accept();
        } catch (SocketException e) {
            System.out.println("done");
        }
        return null;
    }

    private void setDebug(boolean debug) {
        this.debug = debug;
    }

    private class Request {
        String method;
        String version;
        String path;
        String[] metadatas;
        InputStream in;

        Request(Socket sock) throws IOException {
            in = sock.getInputStream();
            header();
            if (debug) {
                System.out.println(this);
                for (String metadata : metadatas) {
                    System.out.println(metadata);
                }
            }
        }

        private void header() throws IOException {
            byte[] buff = new byte[2000];
            for (int i = 0; ; i++) {
                int c = in.read();
                if (c < 0) {
                    throw new BadRequestException("header too short: " + new String(buff, 0, i), "header too short", HttpURLConnection.HTTP_BAD_REQUEST);
                }
                buff[i] = (byte) c;
                if (i > 3 && buff[i - 3] == '\r' && buff[i - 2] == '\n' && buff[i - 1] == '\r' && buff[i] == '\n') {
                    createHeader(buff, i - 4);
                    break;
                } else if (i == buff.length - 1) {
                    if (i > MAX_HEADER_SIZE) {
                        throw new BadRequestException("header too long:" + new String(buff, 0, 256), "header too long", HttpURLConnection.HTTP_BAD_REQUEST);
                    }
                    byte[] nbuff = new byte[buff.length * 2];
                    System.arraycopy(buff, 0, nbuff, 0, i + 1);
                    buff = nbuff;
                }
            }
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
                        throw new BadRequestException(new String(buff, 0, i + 1), "header too long", HttpURLConnection.HTTP_BAD_REQUEST);
                    }
                    metadatas = new String(buff, i + 1, len - i).split("\\r\\n");
                    break;
                }
            }
        }

        public String toString() {
            return super.toString() + LF +
                    method +
                    ' ' + path + " HTTP/" + version;
        }
    }

    private class BadRequestException extends RuntimeException {
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
    }
}
