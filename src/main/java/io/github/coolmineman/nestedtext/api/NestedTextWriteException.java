package io.github.coolmineman.nestedtext.api;

public class NestedTextWriteException extends RuntimeException {
    private static final long serialVersionUID = -5368373633051911177L;

    public NestedTextWriteException(Exception e) {
        super(e);
    }

    public NestedTextWriteException(String s) {
        super(s);
    }
}
