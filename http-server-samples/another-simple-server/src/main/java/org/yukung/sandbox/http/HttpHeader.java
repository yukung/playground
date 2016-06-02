package org.yukung.sandbox.http;

import static org.yukung.sandbox.http.Constant.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Yusuke Ikeda
 */
class HttpHeader {

    private HttpMethod method;
    private String path;
    private final String headerText;
    private Map<String, String> messageHeaders = new HashMap<>();

    HttpHeader(InputStream in) throws IOException {
        this.headerText = readRequestLine(in) + readMessageLine(in);
    }

    String getText() {
        return headerText;
    }

    int getContentLength() {
        return Integer.parseInt(messageHeaders.getOrDefault("Content-Length", "0"));
    }

    boolean isChunkedTransfer() {
        return messageHeaders.getOrDefault("Transfer-Encoding", "-").equals("chunked");
    }

    boolean isGetMethod() {
        return method == HttpMethod.GET;
    }

    String getPath() {
        return path;
    }

    private String readRequestLine(InputStream in) throws IOException {
        String requestLine = IOUtil.readLine(in);
        String[] tmp = requestLine.split(" ");
        method = HttpMethod.valueOf(tmp[0].toUpperCase());
        path = URLDecoder.decode(tmp[1], "UTF-8");
        return requestLine + CRLF;
    }

    private StringBuilder readMessageLine(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();

        String messageLine = IOUtil.readLine(in);

        while (messageLine != null && !messageLine.isEmpty()) {
            putMessageLine(messageLine);

            sb.append(messageLine).append(CRLF);
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
