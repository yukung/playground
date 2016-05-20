package org.yukung.sandbox.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.util.stream.Stream;

/**
 * @author yukung
 */
class HttpRequest {

    private static final String CRLF = "\n";
    private final String headerText;
    private final String bodyText;

    HttpRequest(InputStream input) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(input, "UTF-8"))) {
            headerText = readHeader(br);
            bodyText = readBody(br);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

    }

    String getHeaderText() {
        return headerText;
    }

    String getBodyText() {
        return bodyText;
    }

    private String readHeader(BufferedReader br) throws IOException {
        String line = br.readLine();
        StringBuilder header = new StringBuilder();

        while (line != null && !line.isEmpty()) {
            header.append(line).append("\n");
            line = br.readLine();
        }

        return header.toString();
    }

    private String readBody(BufferedReader br) throws IOException {
        final int contentLength = getContentLength();

        if (contentLength <= 0) {
            return null;
        }
        char[] c = new char[contentLength];
        br.read(c);

        return new String(c);
    }

    private int getContentLength() {
        return Stream.of(this.headerText.split(CRLF))
                .filter(headerLine -> headerLine.startsWith("Content-Length"))
                .map(contentLengthHeader -> contentLengthHeader.split(":")[1].trim())
                .mapToInt(Integer::parseInt)
                .findFirst().orElse(0);
    }
}
