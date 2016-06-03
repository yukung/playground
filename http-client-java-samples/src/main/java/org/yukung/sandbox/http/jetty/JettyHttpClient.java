package org.yukung.sandbox.http.jetty;

import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpStatus;
import org.yukung.sandbox.http.HttpClient;

import java.nio.charset.StandardCharsets;

/**
 * @author yukung
 */
public class JettyHttpClient implements HttpClient {

    private org.eclipse.jetty.client.HttpClient client = new org.eclipse.jetty.client.HttpClient();

    @Override
    public String get(String url) {
        String responseBody = null;
        try {
            client.start();
            ContentResponse response = client.GET(url);
            if (response.getStatus() == HttpStatus.OK_200) {
                responseBody = response.getContentAsString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        close();
        return responseBody;
    }

    @Override
    public String post(String url, String body) {
        String responseBody = null;
        try {
            client.start();
            ContentResponse response = client.POST(url)
                    .content(new StringContentProvider(body, StandardCharsets.UTF_8))
                    .send();
            if (response.getStatus() == HttpStatus.OK_200) {
                responseBody = response.getContentAsString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        close();
        return responseBody;
    }

    @Override
    public void close() {
        if (client != null && !client.isStopped()) {
            try {
                client.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
