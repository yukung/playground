package org.yukung.sandbox.http;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @author yukung
 */
public class HttpServer {

    static final int DEFAULT_PORT = 8800;
    Authority authority;
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private boolean debug;

    HttpServer(int port) throws IOException {
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        ServerSocket serverSocket = serverSocketChannel.socket();
        serverSocket.setReuseAddress(true);
        serverSocket.bind(new InetSocketAddress(port));
        if (serverSocketChannel.isBlocking()) {
            serverSocketChannel = (ServerSocketChannel) serverSocketChannel.configureBlocking(false);
        }
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
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

    boolean isDebugEnabled() {
        return debug;
    }

    private void setDebugEnabled(boolean debug) {
        this.debug = debug;
    }

    synchronized void close() {
        if (serverSocketChannel == null) {
            return;
        }
        try {
            selector.close();
        } catch (IOException e) {
            if (debug) {
                e.printStackTrace();
            }
        }
        try {
            serverSocketChannel.close();
            serverSocketChannel = null;
        } catch (IOException e) {
            if (debug) {
                e.printStackTrace();
            }
        }
    }

    void service() throws IOException {
        service(null);
    }

    void service(String authType) throws IOException {
        assert serverSocketChannel != null && selector != null;
        authority = Authority.newAuthority(authType);

        for (accept(); select(); ) {

        }
    }

    private boolean select() throws IOException {
        try {
            selector.select();
            for (Iterator<SelectionKey> keys = selector.selectedKeys().iterator(); keys.hasNext(); ) {
                SelectionKey key = keys.next();
                keys.remove();
                selected(key);
            }
        } catch (ClosedSelectorException e) {
            return false;
        }
        return true;
    }

    private void selected(SelectionKey key) {
        assert key.isValid() : "key is not valid";
        try {
            if (key.isAcceptable()) {
                accept();
            } else {
                if (key.isReadable()) {
                    ((Request) key.attachment()).readyToRead(key);
                }
                if (key.isValid() && key.isWritable()) {
                    ((Request) key.attachment()).readyToWrite(key);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            ((Request) key.attachment()).abort(key);
        }
    }

    private void accept() throws IOException {
        SocketChannel socketChannel = serverSocketChannel.accept();
        if (socketChannel == null) {
            return;
        }
        if (socketChannel.isBlocking()) {
            socketChannel = (SocketChannel) socketChannel.configureBlocking(false);
        }
        Request request = new Request(socketChannel, this);
        SelectionKey key = socketChannel.register(selector, SelectionKey.OP_READ, request);
        request.readyToRead(key);
    }
}
