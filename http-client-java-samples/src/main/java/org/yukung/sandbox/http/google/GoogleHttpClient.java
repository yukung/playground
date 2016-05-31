package org.yukung.sandbox.http.google;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.apache.ApacheHttpTransport;
import org.yukung.sandbox.http.HttpClient;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author yukung
 */
public class GoogleHttpClient implements HttpClient {

    private HttpTransport httpTransport = new ApacheHttpTransport();

    @Override
    public String get(String url) {
        String responseBody = null;
        HttpRequestFactory requestFactory = httpTransport.createRequestFactory();

        GenericUrl genericUrl = new GenericUrl(url);
        try {
            HttpRequest request = requestFactory.buildGetRequest(genericUrl);
            HttpResponse response = request.execute();
            try {
                responseBody = response.parseAsString();
            } finally {
                try {
                    response.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseBody;
    }

    @Override
    public String post(String url, String body) {
        String responseBody = null;
        HttpRequestFactory requestFactory = httpTransport.createRequestFactory();

        GenericUrl genericUrl = new GenericUrl(url);
        try {
            HttpRequest request = requestFactory.buildPostRequest(genericUrl,
                    new InputStreamContent("text/plain",
                            new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8))));
            HttpResponse response = request.execute();
            try {
                responseBody = response.parseAsString();
            } finally {
                try {
                    response.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseBody;
    }

    @Override
    public void close() {
        try {
            if (httpTransport != null) {
                httpTransport.shutdown();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
