package org.yukung.sandbox.http;

/**
 * @author yukung
 */
enum HttpStatus {
    OK("200 OK"),
    NOT_FOUND("404 Not Found");

    private final String text;

    HttpStatus(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
