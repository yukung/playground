package org.yukung.sandbox.http;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yukung
 */
abstract class Authority {

    static final String BASIC = "basic";

    static final String DIGEST = "digest";

    private static final Pattern AUTHORIZATION =
        Pattern.compile("^Authorization:[ \\t]*(.+)$", Pattern.CASE_INSENSITIVE);

    private static final Pattern BASIC_CREDENTIAL = Pattern.compile("^basic[ \\t]+(.+)$", Pattern.CASE_INSENSITIVE);

    private static final Pattern DIGEST_CREDENTIAL_BASE = Pattern.compile("^Digest[ \\t]+username=\"(.+?)\",\\s*(.+)$");

    private static final Pattern DIGEST_CREDENTIAL_REALM = Pattern.compile("realm=\"(.+?)\"");

    private static final Pattern DIGEST_CREDENTIAL_NONCE = Pattern.compile("nonce=\"(.+?)\"");

    private static final Pattern DIGEST_CREDENTIAL_URI = Pattern.compile("uri=\"(.+?)\"");

    private static final Pattern DIGEST_CREDENTIAL_RESP = Pattern.compile("response=\"(.+?)\"");

    private static final Pattern DIGEST_CREDENTIAL_ALGORITHM = Pattern.compile("algorithm=\"?MD5\"?");

    private static final Pattern DIGEST_CREDENTIAL_QOP = Pattern.compile("qop=\"?auth\"?");

    private static final Pattern DIGEST_CREDENTIAL_NC = Pattern.compile("nc=([0-9a-f]{8})");

    private static final Pattern DIGEST_CREDENTIAL_CNONCE = Pattern.compile("cnonce=\"(.+?)\"");

    private static final String TOHEX = "0123456789abcdef";

    static Authority newAuthority(String type) {
        if (BASIC.equals(type)) {
            return new BasicAuth();
        } else if (DIGEST.equals(type)) {
            return new DigestAuth();
        }
        return new NullAuth();
    }

    static String toLowerHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(TOHEX.charAt((b >> 4) & 0x0f));
            sb.append(TOHEX.charAt(b & 0x0f));
        }
        return sb.toString();
    }

    private static void printRealm(PrintWriter writer) {
        writer.print(" realm=\"");
        writer.print(realm());
        writer.print("\"");
    }

    private static String realm() {
        return Authority.class.getPackage().getName();
    }

    private static String getValue(String credentials, Pattern pattern) {
        Matcher m = pattern.matcher(credentials);
        if (!m.find()) {
            throw new HttpServer.BadRequestException("bad/missing directives");
        }
        return m.group(1);
    }

    private static String md5(String s) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        return toLowerHex(md5.digest(s.getBytes()));
    }

    void authorize(String[] metadatas) {
        for (String metadata : metadatas) {
            Matcher m = AUTHORIZATION.matcher(metadata);
            if (m.find()) {
                if (checkCredentials(m.group(1))) {
                    return;
                }
                break;
            }
        }
        handleNoCredential();
    }

    boolean checkCredentials(String credentials) {
        return false;
    }

    void handleNoCredential() {
        throw new AuthorizationException("Authorization Required");
    }

    abstract void challenge(PrintWriter writer) throws IOException;

    private static class AuthorizationException extends HttpServer.BadRequestException {
        AuthorizationException(String msg) {
            super(msg, HttpURLConnection.HTTP_UNAUTHORIZED);
        }
    }

    private static class BasicAuth extends Authority {
        boolean checkCredentials(String credentials) {
            Matcher m = BASIC_CREDENTIAL.matcher(credentials);
            if (m.find()) {
                String upass = new String(Base64Util.decode(m.group(1)));
                if ("web:2.0".equals(upass)) {
                    return true;
                }
            }
            return super.checkCredentials(credentials);
        }

        @Override
        void challenge(PrintWriter writer) throws IOException {
            writer.print("WWW-Authenticate: ");
            writer.print("Basic");
            printRealm(writer);
        }
    }

    private static class DigestAuth extends Authority {
        boolean checkCredentials(String credentials) {
            Matcher m = DIGEST_CREDENTIAL_BASE.matcher(credentials);
            if (!m.find())
                throw new HttpServer.BadRequestException("bad/missing directives");
            String user = m.group(1);
            String rest = m.group(2);
            String realm = getValue(rest, DIGEST_CREDENTIAL_REALM);
            if (!realm.equals(realm()))
                throw new HttpServer.BadRequestException("bad/missing directives");
            if (!DIGEST_CREDENTIAL_ALGORITHM.matcher(rest).find() || !DIGEST_CREDENTIAL_QOP.matcher(rest).find()) {
                throw new HttpServer.BadRequestException("bad/missing directives");
            }
            String nonce = getValue(rest, DIGEST_CREDENTIAL_NONCE);
            String uri = getValue(rest, DIGEST_CREDENTIAL_URI);
            String resp = getValue(rest, DIGEST_CREDENTIAL_RESP);
            String nc = getValue(rest, DIGEST_CREDENTIAL_NC);
            String cnonce = getValue(rest, DIGEST_CREDENTIAL_CNONCE);

            try {
                String a1 = md5(user + ":" + realm + ":" + "2.0");
                String a2 = md5("GET" + ":" + uri);
                String check = md5(a1 + ":" + nonce + ":" + nc + ":" + cnonce + ":auth:" + a2);
                return check.equals(resp);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        void challenge(PrintWriter writer) throws IOException {
            writer.print("WWW-Authenticate: ");
            writer.print("Digest");
            printRealm(writer);
            writer.print(", ");
            printNonce(writer);
            writer.print(", algorithm=MD5, qop=\"auth\"");
        }

        private void printNonce(PrintWriter writer) {
            writer.print("nonce=\"");
            writer.print(nonce());
            writer.print("\"");
        }

        private String nonce() {
            String seed = Long.toHexString(System.currentTimeMillis()) + Integer.toHexString(hashCode());
            try {
                return md5(seed);
            } catch (NoSuchAlgorithmException e) {
                return seed;
            }
        }

    }

    private static class NullAuth extends Authority {

        boolean checkCredentials(String credentials) {
            return true;
        }

        void handleNoCredential() {
        }

        @Override
        void challenge(PrintWriter writer) throws IOException {
        }

    }
}
