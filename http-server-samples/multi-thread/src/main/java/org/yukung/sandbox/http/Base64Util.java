package org.yukung.sandbox.http;

import java.nio.ByteBuffer;

/**
 * @author yukung
 */
abstract class Base64Util {

    private static final byte[] tb64 = {
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63,
            52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, 0, -1, -1,
            -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14,
            15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1,
            -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
            41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1,
    };

    static byte[] decode(String encoded) {
        ByteBuffer bf = ByteBuffer.allocate(encoded.length());
        for (int i = 0, n = encoded.length(); i < n; ) {
            int x = 0;
            int eq = 0;
            boolean start = false;
            for (int j = 0; i < n && j < 4; j++, i++) {
                char ch = encoded.charAt(i);
                if (Character.isWhitespace(ch)) {
                    j--;
                    continue;
                }
                if (ch > tb64.length || tb64[ch] < 0) {
                    j--;
                    continue;
                }
                x <<= 6;
                if (ch != '=') {
                    if (eq > 0)
                        break;
                    start = true;
                    x |= tb64[ch];
                } else {
                    eq++;
                }
            }
            if (start) {
                bf.put((byte) (x >> 16));
                bf.put((byte) (x >> 8));
                bf.put((byte) x);
                if (eq > 0) {
                    bf.position(bf.position() - eq);
                }
            }
        }
        bf.flip();
        byte[] b = new byte[bf.limit()];
        bf.get(b);
        return b;
    }
}
