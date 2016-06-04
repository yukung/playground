package org.yukung.sandbox.http.okhttp;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.yukung.sandbox.http.HttpClient;

import java.io.IOException;

/**
 * @author yukung
 */
public class OkHttpHttpClient implements HttpClient {

    private OkHttpClient client = new OkHttpClient();

    @Override
    public String get(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        return requestAndResponse(request);
    }

    @Override
    public String post(String url, String body) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), body);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        return requestAndResponse(request);
    }

    @Override
    public void close() {
        // no-op
    }

    private String requestAndResponse(Request request) {
        String responseBody = null;
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                responseBody = response.body().string();
            }
            response.body().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseBody;
    }
}
