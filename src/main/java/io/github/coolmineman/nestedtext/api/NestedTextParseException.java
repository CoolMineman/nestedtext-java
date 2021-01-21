package io.github.coolmineman.nestedtext.api;

public class NestedTextParseException extends RuntimeException {
    private static final long serialVersionUID = -5959728906926303521L;
    
    public NestedTextParseException(Throwable cause) {
        super(cause);
    }

    public NestedTextParseException(String message) {
        super(message);
    }

    public NestedTextParseException(int lineNumber, int column, String message) {
        this("Error at Line " + lineNumber + " Column " + column + ": " + message);
    }
}
