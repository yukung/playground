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

    private static final String LF = "\n";
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
        if (isChunkedTransfer()) {
            return readBodyByChunkedTransfer(br);
        } else {
            return readBodyByContentLength(br);
        }
    }

    private boolean isChunkedTransfer() {
        return Stream.of(headerText.split(LF))
                .filter(headerLine -> headerLine.startsWith("Transfer-Encoding"))
                .map(transferEncoding -> transferEncoding.split(":")[1].trim())
                .anyMatch("chunked"::equals);
    }

    private String readBodyByChunkedTransfer(BufferedReader br) throws IOException {
        StringBuilder body = new StringBuilder();

        int chunkSize = Integer.parseInt(br.readLine(), 16);

        while (chunkSize != 0) {
            char[] buffer = new char[chunkSize];
            br.read(buffer);

            body.append(buffer);

            br.readLine();  // Skip the CRLF at the end of the chunk-body
            chunkSize = Integer.parseInt(br.readLine(), 16);
        }
        return body.toString();
    }

    private String readBodyByContentLength(BufferedReader br) throws IOException {
        final int contentLength = getContentLength();

        if (contentLength <= 0) {
            return null;
        }
        char[] c = new char[contentLength];
        br.read(c);

        return new String(c);
    }

    private int getContentLength() {
        return Stream.of(this.headerText.split(LF))
                .filter(headerLine -> headerLine.startsWith("Content-Length"))
                .map(contentLengthHeader -> contentLengthHeader.split(":")[1].trim())
                .mapToInt(Integer::parseInt)
                .findFirst().orElse(0);
    }
}
