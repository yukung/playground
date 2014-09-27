package org.yukung.sandbox.dropwizard.sample.core;

/**
 * @author yukung
 */
public class User {
    private final String name;

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
