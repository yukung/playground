package org.yukung.sandbox.http.jaxrs;

import org.yukung.sandbox.http.HttpClient;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

/**
 * @author yukung
 */
public class JaxRsHttpClient implements HttpClient {

    private Client client;

    @Override
    public String get(String url) {
        String responseBody = null;
        try {
            client = ClientBuilder.newClient();
            Response response = client.target(url).request().get();
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                responseBody = response.readEntity(String.class);
            }
            response.close();
        } finally {
            close();
        }
        return responseBody;
    }

    @Override
    public String post(String url, String body) {
        String responseBody = null;
        try {
            client = ClientBuilder.newClient();
            Response response = client.target(url).request().post(Entity.text(body));
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                responseBody = response.readEntity(String.class);
            }
            response.close();
        } finally {
            close();
        }
        return responseBody;
    }

    @Override
    public void close() {
        if (client != null) {
            client.close();
        }
    }
}
