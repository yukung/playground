package org.yukung.sandbox.http;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author yukung
 */
class HttpResponse {

    private final HttpStatus status;
    private Map<String, String> headers = new HashMap<>();
    private String body;

    HttpResponse(HttpStatus status) {
        this.status = Objects.requireNonNull(status);
    }

    void addHeader(String string, Object value) {
        headers.put(string, value.toString());
    }

    void setBody(String body) {
        this.body = body;
    }

    void writeTo(OutputStream out) {
        IOUtil.println(out, "HTTP/1.1 " + status);

        headers.forEach((key, value) -> {
            IOUtil.println(out, key + ": " + value);
        });

        if (body != null) {
            IOUtil.println(out, "");
            IOUtil.print(out, body);
        }
    }
}
