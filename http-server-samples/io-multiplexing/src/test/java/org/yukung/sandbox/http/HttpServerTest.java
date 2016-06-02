package org.yukung.sandbox.http;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        Thread t = new Thread(() -> {
            try {
                server.service();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        t.start();
        Thread.yield();
        HttpURLConnection cli =
                (HttpURLConnection) new URL("http://localhost:" + HttpServer.DEFAULT_PORT + "/test").openConnection();
        assertThat(cli.getResponseCode(), is(HttpURLConnection.HTTP_NOT_FOUND));
        cli.disconnect();
        assertThat(t.isAlive(), is(true));
        server.close();
        Thread.sleep(200);
        assertThat(t.isAlive(), is(false));
    }

    @Test
    public void testServiceExit() throws Exception {
        Thread t = new Thread(() -> {
            try {
                server.service();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        t.start();
        Thread.yield();
        HttpURLConnection cli =
                (HttpURLConnection) new URL("http://localhost:" + HttpServer.DEFAULT_PORT + "/quit").openConnection();
        assertThat(cli.getResponseCode(), is(HttpURLConnection.HTTP_OK));
        cli.disconnect();
        Thread.sleep(200);
        assertThat(t.isAlive(), is(false));
    }

    @Test
    public void testBasicAuth() throws Exception {
        Thread t = new Thread(() -> {
            try {
                server.service(Authority.BASIC);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        t.start();
        Thread.yield();
        HttpURLConnection cli =
                (HttpURLConnection) new URL("http://localhost:" + HttpServer.DEFAULT_PORT + "/quit").openConnection();
        assertThat(cli.getResponseCode(), is(HttpURLConnection.HTTP_UNAUTHORIZED));
        cli.disconnect();
        cli = (HttpURLConnection) new URL("http://localhost:" + HttpServer.DEFAULT_PORT + "/quit").openConnection();
        cli.setRequestProperty("Authorization", "Basic d2ViOjIuMA==");
        assertThat(cli.getResponseCode(), is(HttpURLConnection.HTTP_OK));
        cli.disconnect();
    }

    @Test
    public void testDigestAuth() throws Exception {
        Thread t = new Thread(() -> {
            try {
                server.service(Authority.DIGEST);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        t.start();
        Thread.yield();
        HttpURLConnection cli =
                (HttpURLConnection) new URL("http://localhost:" + HttpServer.DEFAULT_PORT + "/quit").openConnection();
        assertThat(cli.getResponseCode(), is(HttpURLConnection.HTTP_UNAUTHORIZED));
        String challenge = cli.getHeaderField("WWW-Authenticate");
        Matcher m =
                Pattern.compile("Digest realm=\"(.+?)\",\\s*nonce=\"(.+?)\",\\s*algorithm=MD5,\\s*qop=\"auth\"$",
                        Pattern.CASE_INSENSITIVE).matcher(challenge);
        assertThat("unmatched:" + challenge, m.find(), is(true));
        cli.disconnect();
        cli = (HttpURLConnection) new URL("http://localhost:" + HttpServer.DEFAULT_PORT + "/quit").openConnection();
        StringBuilder sb = new StringBuilder("Digest username=\"web\",realm=\"");
        sb.append(m.group(1)).append("\", nonce=\"").append(m.group(2)).append("\", uri=\"/quit\", ");
        sb.append("qop=auth, nc=00000001, algorithm=MD5, cnonce=\"").append(Integer.toHexString(hashCode()));
        String a1 = "web:" + m.group(1) + ":2.0";
        String a2 = "GET:/quit";
        String cred = md5(a1) + ":" + m.group(2) + ":00000001:" + Integer.toHexString(hashCode()) + ":auth:" + md5(a2);
        sb.append("\", response=\"").append(md5(cred)).append("\"");
        cli.setRequestProperty("Authorization", sb.toString());
        assertThat(cli.getResponseCode(), is(HttpURLConnection.HTTP_OK));
        cli.disconnect();
    }

    private String md5(String s) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        return Authority.toLowerHex(md5.digest(s.getBytes()));
    }
}
