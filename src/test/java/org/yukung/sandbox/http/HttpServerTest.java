package org.yukung.sandbox.http;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * @author yukung
 */
public class HttpServerTest {

    private HttpServer server;

    @Before
    public void setUp() throws Exception {
        server = new HttpServer(HttpServer.DEFAULT_PORT);
    }

    @After
    public void tearDown() throws Exception {
        server.close();
    }

    @Test
    public void testService() throws Exception {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    server.service();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        Thread.yield();
        HttpURLConnection cli = (HttpURLConnection) new URL("http://localhost:" + HttpServer.DEFAULT_PORT + "/test").openConnection();
        assertThat(cli.getResponseCode(), is(HttpURLConnection.HTTP_NOT_FOUND));
        cli.disconnect();
        assertThat(t.isAlive(), is(true));
        server.close();
        Thread.yield();
        assertThat(t.isAlive(), is(false));
    }

    @Test
    public void testServiceExit() throws Exception {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    server.service();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        Thread.yield();
        HttpURLConnection cli = (HttpURLConnection) new URL("http://localhost:" + HttpServer.DEFAULT_PORT + "/quit").openConnection();
        assertThat(cli.getResponseCode(), is(HttpURLConnection.HTTP_OK));
        cli.disconnect();
        t.sleep(200);
        assertThat(t.isAlive(), is(false));
    }
}
