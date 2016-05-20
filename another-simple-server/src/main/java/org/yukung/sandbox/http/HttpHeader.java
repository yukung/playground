package org.yukung.sandbox.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Yusuke Ikeda
 */
public class HttpHeader {

    private final String headerText;
    private Map<String, String> messageHeaders = new HashMap<>();

    public HttpHeader(InputStream in) throws IOException {
        StringBuilder header = new StringBuilder();

        header.append(readRequestLine(in));
        header.append(readMessageLine(in));

        this.headerText = header.toString();
    }

    public String getText() {
        return headerText;
    }

    public int getContentLength() {
        return Integer.parseInt(messageHeaders.getOrDefault("Content-Length", "0"));
    }

    public boolean isChunkedTransfer() {
        return messageHeaders.getOrDefault("Transfer-Encoding", "-").equals("chunked");
    }

    private String readRequestLine(InputStream in) throws IOException {
        return IOUtil.readLine(in) + HttpRequest.CRLF;
    }

    private StringBuilder readMessageLine(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();

        String messageLine = IOUtil.readLine(in);

        while (messageLine != null && !messageLine.isEmpty()) {
            putMessageLine(messageLine);

            sb.append(messageLine + HttpRequest.CRLF);
            messageLine = IOUtil.readLine(in);
        }
        return sb;
    }

    private void putMessageLine(String messageLine) {
        String[] tmp = messageLine.split(":");
        String key = tmp[0].trim();
        String value = tmp[1].trim();
        messageHeaders.put(key, value);
    }
}
