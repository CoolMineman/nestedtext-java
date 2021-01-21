package io.github.coolmineman.nestedtext.api;

public class TabException extends RuntimeException {
    private static final long serialVersionUID = 8701684004540093899L;

    public TabException() {
        super("You indented a tab; that is not allowed");
    }
}
