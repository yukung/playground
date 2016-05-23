package org.yukung.sandbox.http;

/**
 * @author yukung
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("start >>>");

        SimpleHttpServer server = new SimpleHttpServer();
        server.start();

        System.out.println("<<< end");
    }
}
