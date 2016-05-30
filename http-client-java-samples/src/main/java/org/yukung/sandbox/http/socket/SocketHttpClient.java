package org.yukung.sandbox.http.socket;

import org.yukung.sandbox.http.HttpClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * @author yukung
 */
public class SocketHttpClient implements HttpClient {

    @Override
    public String get(String url) {
        StringBuilder sb = new StringBuilder();
        try {
            URL canonicalUrl = new URL(url);
            try (Socket socket = new Socket(canonicalUrl.getHost(), canonicalUrl.getPort());
                 OutputStreamWriter out = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8);
                 BufferedWriter writer = new BufferedWriter(out);
                 InputStreamReader in = new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8);
                 BufferedReader reader = new BufferedReader(in)
            ) {
                writer.write("GET " + canonicalUrl.getPath() + "?" + canonicalUrl.getQuery() + " HTTP/1.1\r\n");
                writer.write("Connection: close\r\n");
                writer.write("\r\n");
                writer.flush();

                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\r\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    @Override
    public String post(String url, String body) {
        StringBuilder sb = new StringBuilder();
        try {
            URL canonicalUrl = new URL(url);
            try (Socket socket = new Socket(canonicalUrl.getHost(), canonicalUrl.getPort());
                 OutputStreamWriter out = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8);
                 BufferedWriter writer = new BufferedWriter(out);
                 InputStreamReader in = new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8);
                 BufferedReader reader = new BufferedReader(in)
            ) {
                writer.write("POST " + canonicalUrl.getPath() + " HTTP/1.1\r\n");
                writer.write("Connection: close\r\n");
                writer.write("Content-Length: " + body.length() + "\r\n");
                writer.write("Content-Type: text/plain\r\n");
                writer.write("\r\n");
                writer.write(body);
                writer.flush();

                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\r\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    @Override
    public void close() {
        // no-op
    }
}
