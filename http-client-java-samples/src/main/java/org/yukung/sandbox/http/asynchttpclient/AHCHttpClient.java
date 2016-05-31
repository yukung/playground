package org.yukung.sandbox.http.asynchttpclient;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.Response;
import org.yukung.sandbox.http.HttpClient;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * @author yukung
 */
public class AHCHttpClient implements HttpClient {

    @Override
    public String get(String url) {
        String responseBody = null;
        try (AsyncHttpClient client = new DefaultAsyncHttpClient()) {
            Response response = client.prepareGet(url).execute().get();
            if (response.getStatusCode() == HttpResponseStatus.OK.code()) {
                responseBody = response.getResponseBody(CharsetUtil.UTF_8);
            }
        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return responseBody;
    }

    @Override
    public String post(String url, String body) {
        String responseBody = null;
        try (AsyncHttpClient client = new DefaultAsyncHttpClient()) {
            Response response = client.preparePost(url).setBody(body).execute().get();
            if (response.getStatusCode() == HttpResponseStatus.OK.code()) {
                responseBody = response.getResponseBody(CharsetUtil.UTF_8);
            }
        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return responseBody;
    }

    @Override
    public void close() {
        // no-op
    }
}
