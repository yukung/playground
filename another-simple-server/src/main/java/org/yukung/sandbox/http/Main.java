package org.yukung.sandbox.http;

import java.io.IOException;
import java.io.InputStream;
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
                InputStream in = socket.getInputStream()
        ) {
            HttpRequest request = new HttpRequest(in);

            System.out.println(request.getHeaderText());
            System.out.println(request.getBodyText());
        }

        System.out.println("<<< end");
    }
}
