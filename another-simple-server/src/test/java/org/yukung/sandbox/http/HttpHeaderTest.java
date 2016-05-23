package org.yukung.sandbox.http;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.yukung.sandbox.http.Constant.CRLF;

import org.junit.Test;

/**
 * @author Yusuke Ikeda
 */
public class HttpHeaderTest {

    @Test
    public void method_get() throws Exception {
        String headerText = "GET /foo/bar HTTP/1.1" + CRLF
            + CRLF;

        HttpHeader header = new HttpHeader(IOUtil.toInputStream(headerText));

        assertThat(header.isGetMethod(), is(true));
    }

    @Test
    public void path() throws Exception {
        String headerText = "GET /foo/bar HTTP/1.1" + CRLF
            + CRLF;

        HttpHeader header = new HttpHeader(IOUtil.toInputStream(headerText));

        assertThat(header.getPath(), is("/foo/bar"));
    }

    @Test
    public void path_url_encoding() throws Exception {
        String headerText = "GET /foo/%e3%83%86%e3%82%b9%e3%83%88/bar HTTP/1.1" + CRLF
            + CRLF;

        HttpHeader header = new HttpHeader(IOUtil.toInputStream(headerText));

        assertThat(header.getPath(), is("/foo/テスト/bar"));
    }

    @Test
    public void text() throws Exception {
        String headerText =
            "POST /chunk.txt HTTP/1.1" + CRLF
                + "User-Agent: curl/7.37.1" + CRLF
                + "Host: localhost" + CRLF
                + "Accept: */*" + CRLF
                + "Transfer-Encoding: chunked" + CRLF
                + "Expect: 100-continue" + CRLF
                + CRLF;

        HttpHeader header = new HttpHeader(IOUtil.toInputStream(headerText));

        assertThat(header.getText() + CRLF, is(headerText));
    }


    @Test
    public void contentLength() throws Exception {
        String headerText =
            "POST / HTTP/1.1" + CRLF
                + "User-Agent: curl/7.37.1" + CRLF
                + "Host: localhost" + CRLF
                + "Accept: */*" + CRLF
                + "Content-Length: 14" + CRLF
                + "Content-Type: application/x-www-form-urlencoded" + CRLF
                + CRLF;

        HttpHeader header = new HttpHeader(IOUtil.toInputStream(headerText));

        assertThat(header.getContentLength(), is(14));
    }


    @Test
    public void chunked() throws Exception {
        String headerText =
            "POST /chunk.txt HTTP/1.1" + CRLF
                + "User-Agent: curl/7.37.1" + CRLF
                + "Host: localhost" + CRLF
                + "Accept: */*" + CRLF
                + "Transfer-Encoding: chunked" + CRLF
                + "Expect: 100-continue" + CRLF
                + CRLF;

        HttpHeader header = new HttpHeader(IOUtil.toInputStream(headerText));

        assertThat(header.isChunkedTransfer(), is(true));
    }
}