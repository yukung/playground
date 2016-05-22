package org.yukung.sandbox.http;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import static java.lang.System.*;

/**
 * @author yukung
 */
public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("start >>>");

        try (
            ServerSocket ss = new ServerSocket(8080);
            Socket socket = ss.accept();
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream()
        ) {
            HttpRequest request = new HttpRequest(in);
            HttpResponse response = new HttpResponse(HttpStatus.OK);
            HttpHeader header = request.getHeader();

            if (header.isGetMethod()) {
                File file = new File(".", header.getPath());
                if (file.exists() && file.isFile()) {
                    respondLocalFile(file, out);
                } else {
                    respondNotFoundError(out);
                }
            } else {
                respondOk(out);
            }
        }

        out.println("<<< end");
    }

    private static void respondOk(OutputStream out) throws IOException {
        HttpResponse response = new HttpResponse(HttpStatus.OK);
        response.writeTo(out);
    }

    private static void respondNotFoundError(OutputStream out) throws IOException {
        HttpResponse response = new HttpResponse(HttpStatus.NOT_FOUND);
        response.addHeader("Content-Type", ContentType.TEXT_PLAIN);
        response.setBody("404 Not Found");
        response.writeTo(out);
    }

    private static void respondLocalFile(File file, OutputStream out) throws IOException {
        HttpResponse response = new HttpResponse(HttpStatus.OK);
        response.setBody(file);
        response.writeTo(out);
    }
}
