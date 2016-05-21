package org.yukung.sandbox.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

/**
 * @author yukung
 */
class HttpRequest {

    private final HttpHeader header;
    private final String bodyText;

    HttpRequest(InputStream input) {
        try {
            header = new HttpHeader(input);
            bodyText = readBody(input);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

    }

    String getHeaderText() {
        return header.getText();
    }

    HttpHeader getHeader() {
        return header;
    }

    String getBodyText() {
        return bodyText;
    }

    private String readBody(InputStream in) throws IOException {
        if (header.isChunkedTransfer()) {
            return readBodyByChunkedTransfer(in);
        } else {
            return readBodyByContentLength(in);
        }
    }

    private String readBodyByChunkedTransfer(InputStream in) throws IOException {
        StringBuilder body = new StringBuilder();

        int chunkSize = Integer.parseInt(IOUtil.readLine(in), 16);

        while (chunkSize != 0) {
            byte[] buffer = new byte[chunkSize];
            in.read(buffer);

            body.append(IOUtil.toString(buffer));

            IOUtil.readLine(in);  // Skip the CRLF at the end of the chunk-body
            chunkSize = Integer.parseInt(IOUtil.readLine(in), 16);
        }
        return body.toString();
    }

    private String readBodyByContentLength(InputStream in) throws IOException {
        final int contentLength = header.getContentLength();

        if (contentLength <= 0) {
            return null;
        }
        byte[] buffer = new byte[contentLength];
        in.read(buffer);

        return IOUtil.toString(buffer);
    }
}
