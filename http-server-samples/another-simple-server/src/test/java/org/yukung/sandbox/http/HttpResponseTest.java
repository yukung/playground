package org.yukung.sandbox.http;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.yukung.sandbox.http.Constant.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * @author Yusuke Ikeda
 */
public class HttpResponseTest {

    @Rule
    public TemporaryFolder tmpDir = new TemporaryFolder();

    @Test
    public void file_html() throws Exception {
        File tmpFile = new File(tmpDir.getRoot(), "test.html");
        Files.write(tmpFile.toPath(), "hello!!".getBytes("UTF-8"), StandardOpenOption.CREATE);

        HttpResponse response = new HttpResponse(HttpStatus.OK);
        response.setBody(tmpFile);

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        response.writeTo(out);

        String actual = out.toString("UTF-8");

        assertThat(actual, is(
            "HTTP/1.1 200 OK" + CRLF +
                "Content-Type: text/html" + CRLF +
                CRLF +
                "hello!!"
        ));
    }

    @Test
    public void basic() throws Exception {
        HttpResponse response = new HttpResponse(HttpStatus.OK);
        response.addHeader("Content-Type", ContentType.TEXT_HTML);
        response.setBody("<h1>Hello World!!</h1>");

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        response.writeTo(out);

        String actual = out.toString("UTF-8");
        assertThat(actual, is(
            "HTTP/1.1 200 OK" + CRLF +
                "Content-Type: text/html" + CRLF +
                CRLF +
                "<h1>Hello World!!</h1>"
        ));
    }
}