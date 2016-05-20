package org.yukung.sandbox.http;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yukung
 */
public final class IOUtil {

    public static final Charset UTF_8 = StandardCharsets.UTF_8;

    public static String readLine(InputStream in) throws IOException {
        List<Byte> bytes = new ArrayList<>();

        while (true) {
            byte b = (byte) in.read();
            bytes.add(b);

            int size = bytes.size();
            if (2 <= size) {
                char cr = (char) bytes.get(size - 2).byteValue();
                char lf = (char) bytes.get(size - 1).byteValue();

                if (cr == '\r' && lf == '\n') {
                    break;
                }
            }
        }

        byte[] buffer = new byte[bytes.size() - 2];// for CRLF
        for (int i = 0; i < bytes.size() - 2; i++) {
            buffer[i] = bytes.get(i);
        }

        return new String(buffer, UTF_8);
    }

    private IOUtil() {
    }

    static String toString(byte[] buffer) {
        return new String(buffer, UTF_8);
    }
}
