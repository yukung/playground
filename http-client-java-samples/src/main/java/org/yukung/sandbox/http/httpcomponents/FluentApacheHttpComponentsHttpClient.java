package org.yukung.sandbox.http.httpcomponents;

import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.StringEntity;
import org.yukung.sandbox.http.HttpClient;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author yukung
 */
public class FluentApacheHttpComponentsHttpClient implements HttpClient {
    @Override
    public String get(String url) {
        String responseBody = null;
        Response response = null;
        try {
            response = Request.Get(url).execute();
            responseBody = response.returnContent().asString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                response.discardContent();
            }
        }
        return responseBody;
    }

    @Override
    public String post(String url, String body) {
        String responseBody = null;
        Response response = null;
        try {
            response = Request.Post(url).body(new StringEntity(body)).execute();
            responseBody = response.returnContent().asString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                response.discardContent();
            }
        }
        return responseBody;
    }

    @Override
    public void close() {
        // no-op
    }
}
