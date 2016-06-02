package org.yukung.sandbox.http;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
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
    private File bodyFile;

    HttpResponse(HttpStatus status) {
        this.status = Objects.requireNonNull(status);
    }

    void addHeader(String string, Object value) {
        headers.put(string, value.toString());
    }

    void setBody(String body) {
        this.body = body;
    }

    void setBody(File file) {
        Objects.requireNonNull(file);
        bodyFile = file;

        String fileName = bodyFile.getName();
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);

        addHeader("Content-Type", ContentType.toContentType(extension));
    }

    void writeTo(OutputStream out) throws IOException {
        IOUtil.println(out, "HTTP/1.1 " + status);

        headers.forEach((key, value) -> {
            IOUtil.println(out, key + ": " + value);
        });

        if (body != null) {
            IOUtil.println(out, "");
            IOUtil.print(out, body);
        } else if (bodyFile != null) {
            IOUtil.println(out, "");
            Files.copy(bodyFile.toPath(), out);
        }
    }
}
