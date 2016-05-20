package org.yukung.sandbox.http;

import static java.lang.System.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

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

            System.out.println(request.getHeaderText());
            System.out.println(request.getBodyText());

            HttpResponse response = new HttpResponse(HttpStatus.OK);
            response.addHeader("Content-Type", ContentType.TEXT_HTML);
            response.setBody("<h1>Hello World!!</h1>");

            response.writeTo(out);
        }

        out.println("<<< end");
    }
}
