package org.yukung.sandbox.http;

import static org.yukung.sandbox.http.Constant.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yukung
 */
final class IOUtil {

    private static final Charset UTF_8 = StandardCharsets.UTF_8;

    static String readLine(InputStream in) throws IOException {
        List<Byte> bytes = new ArrayList<>();

        while (true) {
            byte b = (byte) in.read();

            if (b == -1) {
                throw new EmptyRequestException();
            }

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

    static InputStream toInputStream(String string) {
        return new ByteArrayInputStream(string.getBytes(UTF_8));
    }

    static String toString(byte[] buffer) {
        return new String(buffer, UTF_8);
    }

    static void println(OutputStream out, String line) {
        print(out, line + CRLF);
    }

    static void print(OutputStream out, String line) {
        try {
            out.write(line.getBytes(UTF_8));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private IOUtil() {
    }
}
