package org.yukung.sandbox.http.standard;

import org.yukung.sandbox.http.HttpClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * @author yukung
 */
public class JavaStandardLibraryHttpClient implements HttpClient {

    private HttpURLConnection connection;

    @Override
    public String get(String url) {
        String response = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            response = respond(connection);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return response;
    }

    @Override
    public String post(String url, String body) {
        String response = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.connect();

            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8));
            writer.write(body);
            writer.flush();

            response = respond(connection);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return response;
    }

    @Override
    public void close() {
        if (connection != null) {
            connection.disconnect();
        }
    }

    private String respond(HttpURLConnection connection) throws IOException {
        StringBuilder sb = new StringBuilder();
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            try (InputStreamReader in = new InputStreamReader(connection.getInputStream());
                 BufferedReader reader = new BufferedReader(in)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }
        }
        return sb.toString();
    }
}
