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
                response.setBody(new File(".", header.getPath()));
            }

            response.writeTo(out);
        }

        out.println("<<< end");
    }
}
