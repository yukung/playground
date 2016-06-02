package org.yukung.sandbox.http;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Yusuke Ikeda
 */
abstract class ClientService {
    static final String THREAD_TYPE_KEY = "http-server.thread";

    public static final String THREAD_TYPE_SINGLE = "s";

    static final String THREAD_TYPE_MULTI = "m";

    static final String THREAD_TYPE_POOL = "p";

    private static HttpServer httpServer;

    static ClientService newService(HttpServer httpServer) {
        ClientService.httpServer = httpServer;
        String type = System.getProperty(THREAD_TYPE_KEY, THREAD_TYPE_POOL);
        switch (type.charAt(0)) {
            case 'p':
                if (httpServer.isDebugEnabled()) {
                    System.out.println("service type=thread pool");
                }
                return new PooledClientServiceImpl();
            case 'm':
                if (httpServer.isDebugEnabled()) {
                    System.out.println("service type=multi thread");
                }
                return new MtClientServiceImpl();
            default:
                if (httpServer.isDebugEnabled()) {
                    System.out.println("service type=single thread");
                }
                return new StClientServiceImpl();
        }
    }

    abstract void doService(Socket socket);

    void stop() {
        // noop
    }

    private static class StClientServiceImpl extends ClientService {

        StClientServiceImpl() {
        }

        @Override
        void doService(Socket socket) {
            try {
                httpServer.requestAndResponse(socket);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private static class MtClientServiceImpl extends StClientServiceImpl {

        MtClientServiceImpl() {
        }
        @Override
        void doService(Socket socket) {
            new Thread(() -> {
                threadService(socket);
            }).start();
        }
        void threadService(Socket socket) {
            super.doService(socket);
        }
    }

    private static class PooledClientServiceImpl extends MtClientServiceImpl {
        static final int MAX_THREAD = 3;
        List<Thread> threads;
        volatile List<Socket> clients;
        PooledClientServiceImpl() {
            clients = new ArrayList<>();
            threads = Collections.synchronizedList(new ArrayList<>());
            for (int i = 0; i < MAX_THREAD; i++) {
                threads.add(newThread());
            }
        }
        @Override
        void doService(Socket socket) {
            synchronized (clients) {
                clients.add(socket);
                clients.notifyAll();
            }
        }
        @Override
        void stop() {
            synchronized (clients) {
                for (Socket client : clients) {
                    try {
                        client.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                clients.clear();
                for (int i = 0; i < threads.size(); i++) {
                    clients.add(null);
                }
                clients.notifyAll();
            }
        }
        Thread newThread() {
            Thread t = new Thread(() -> {
                for (; ; ) {
                    try {
                        Socket sock = null;
                        synchronized (clients) {
                            while (clients.size() == 0) {
                                clients.wait();
                            }
                            sock = clients.remove(0);
                        }
                        if (sock == null) {
                            break;
                        }
                        threadService(sock);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                threads.remove(Thread.currentThread());
            });
            t.start();
            return t;
        }
    }
}
