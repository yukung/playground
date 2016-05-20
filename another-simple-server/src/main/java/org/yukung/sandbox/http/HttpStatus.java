package org.yukung.sandbox.http;

/**
 * @author yukung
 */
enum HttpStatus {
    OK("200 OK")
    ;

    private final String text;

    private HttpStatus(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
