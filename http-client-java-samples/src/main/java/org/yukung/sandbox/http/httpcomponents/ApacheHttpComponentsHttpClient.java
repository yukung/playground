package org.yukung.sandbox.http.httpcomponents;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.yukung.sandbox.http.HttpClient;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author yukung
 */
public class ApacheHttpComponentsHttpClient implements HttpClient {

    @Override
    public String get(String url) {
        String responseBody = null;
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(url);
            try (CloseableHttpResponse response = client.execute(httpGet)) {
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    HttpEntity entity = response.getEntity();
                    responseBody = EntityUtils.toString(entity, StandardCharsets.UTF_8);
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
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new StringEntity(body, StandardCharsets.UTF_8));
            try (CloseableHttpResponse response = client.execute(httpPost)) {
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    HttpEntity entity = response.getEntity();
                    responseBody = EntityUtils.toString(entity, StandardCharsets.UTF_8);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseBody;
    }

    @Override
    public void close() {
        // no-op
    }
}
