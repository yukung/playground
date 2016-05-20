package org.yukung.sandbox.http;

import static org.yukung.sandbox.http.HttpRequest.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
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
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))
        ) {
            HttpRequest request = new HttpRequest(in);

            System.out.println(request.getHeaderText());
            System.out.println(request.getBodyText());

            bw.write("HTTP/1.1 200 OK" + CRLF);
            bw.write("Content-Type: text/html" + CRLF);
            bw.write(CRLF);
            bw.write("<h1>Hello World!</h1>");
        }

        System.out.println("<<< end");
    }
}
