package org.yukung.sandbox.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
        ) {
            br.lines().forEach(System.out::println);
        }

        System.out.println("<<< end");
    }
}
