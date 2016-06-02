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

    private static final Pattern LINEAR_WHITE_SPACE = Pattern.compile("\\r\\n[ \\t]+");
    static final int DEFAULT_PORT = 8800;
    private static final Pattern COMMAND = Pattern.compile("^(\\w+)\\s+(.+?)\\s+HTTP/([\\d.]+)$");
    private static final String LF = System.getProperty("line.separator");
    private static final int MAX_HEADER_SIZE = 16384;
    private ServerSocket serverSocket;
    private boolean debug;
    private Authority authority;

    HttpServer(int port) throws IOException {
        serverSocket = new ServerSocket();
        serverSocket.setReuseAddress(true);
        serverSocket.bind(new InetSocketAddress(port));
    }

    private void setDebugEnabled(boolean debug) {
        this.debug = debug;
    }

    boolean isDebugEnabled() {
        return debug;
    }

    synchronized void close() {
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
        service(null);
    }

    void service(String authType) throws IOException {
        assert serverSocket != null;
        authority = Authority.newAuthority(authType);

        ClientService service = ClientService.newService(this);

        for (;;) {
            Socket sock = accept();
            if (sock == null) {
                break;
            }
            service.doService(sock);
        }
        service.stop();
    }

    void requestAndResponse(Socket socket) throws IOException {
        try {
            Request req = new Request(socket);
            if (req.path.equals("/quit")) {
                response(200, "OK", socket.getOutputStream());
                close();
            } else {
                response(req, socket.getOutputStream());
            }
        } catch (BadRequestException e) {
            if (debug) {
                e.printStackTrace();
            }
            response(socket.getOutputStream(), e);
        }
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

    private void response(OutputStream out, BadRequestException e) throws IOException {
        response(e.statusCode, e.responseMessage, out);
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

    private void response(int status, String msg, OutputStream out) throws IOException {
        PrintWriter pw = new PrintWriter(out);
        pw.print("HTTP/1.1 ");
        pw.print(status);
        pw.print(" ");
        pw.print(msg);
        pw.print("\r\n");
        authority.challenge(pw);
        pw.print("\r\n\r\n");
        pw.flush();
    }

    private Socket accept() throws IOException {
        ServerSocket s = this.serverSocket;
        if (s == null) {
            return null;
        }
        try {
            return s.accept();
        } catch (SocketException e) {
            System.out.println("done");
        }
        return null;
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
        server.setDebugEnabled(debug);
        server.service(null/*Authority.DIGEST*/);
        server.close();
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

    private class Request {
        String method;
        String version;
        String path;
        String[] metadatas;
        InputStream in;

        Request(Socket sock) throws IOException {
            assert authority != null;
            in = sock.getInputStream();
            header();
            if (debug) {
                System.out.println(this);
                for (String metadata : metadatas) {
                    System.out.println(metadata);
                }
            }
            authority.authorize(metadatas);
        }

        private void header() throws IOException {
            byte[] buff = new byte[2000];
            for (int i = 0; ; i++) {
                int c = in.read();
                if (c < 0) {
                    throw new BadRequestException("header too short: " + new String(buff, 0, i), "header too short",
                            HttpURLConnection.HTTP_BAD_REQUEST);
                }
                buff[i] = (byte) c;
                if (i > 3 && buff[i - 3] == '\r' && buff[i - 2] == '\n' && buff[i - 1] == '\r' && buff[i] == '\n') {
                    createHeader(buff, i - 4);
                    break;
                } else if (i == buff.length - 1) {
                    if (i > MAX_HEADER_SIZE) {
                        throw new BadRequestException("header too long:" + new String(buff, 0, 256), "header too long",
                                HttpURLConnection.HTTP_BAD_REQUEST);
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
                        throw new BadRequestException(new String(buff, 0, i + 1), "header too long",
                                HttpURLConnection.HTTP_BAD_REQUEST);
                    }
                    String headers = LINEAR_WHITE_SPACE.matcher(new String(buff, i + 1, len - i)).replaceAll(" ");
                    metadatas = headers.split("\\r\\n");
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
}
