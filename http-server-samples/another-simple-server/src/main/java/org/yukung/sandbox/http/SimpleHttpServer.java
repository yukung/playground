package org.yukung.sandbox.http;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class SimpleHttpServer {

    private ExecutorService service = Executors.newCachedThreadPool();

    void start() {
        try (ServerSocket server = new ServerSocket(8080)) {
            while (true) {
                this.serverProcess(server);
            }
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }

    }

    private void serverProcess(ServerSocket server) throws IOException {
        Socket socket = server.accept();

        service.execute(() -> {
            try (
                InputStream in = socket.getInputStream();
                OutputStream out = socket.getOutputStream()
            ) {
                HttpRequest request = new HttpRequest(in);
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
            } catch (EmptyRequestException e) {
                e.printStackTrace(System.err);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
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