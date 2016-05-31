package org.yukung.sandbox.http.jaxrs;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author yukung
 */
public class JaxRsHttpClientTest {

    private JaxRsHttpClient client;

    @Before
    public void setUp() throws Exception {
        client = new JaxRsHttpClient();
    }

    @After
    public void tearDown() throws Exception {
        client.close();
    }

    @Test
    public void get() throws Exception {
        String response = client.get("http://localhost:8080/get?param=value");
        assertThat(response).contains(
                "Accessed URL = /get?param=value",
                "Accessed Method = GET",
                "Accessed Date = ");
    }

    @Test
    public void post() throws Exception {
        String body = "POST Body" + "\r\n" +
                "Hello Http Server!" + "\r\n";

        String response = client.post("http://localhost:8080/post", body);
        assertThat(response).contains(
                "Accessed URL = /post",
                "Accessed Method = POST",
                "Accessed Date =",
                "Request Body<<",
                "POST Body",
                "Hello Http Server!",
                ">>");
    }
}
