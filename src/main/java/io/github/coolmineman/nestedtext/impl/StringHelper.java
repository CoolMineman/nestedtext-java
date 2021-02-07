package io.github.coolmineman.nestedtext.impl;

public class StringHelper {
    private StringHelper() { }

    public static String stripTrailing(String string) {
        int length = string.length();
        for (; length > 0; length--) {
            if (!Character.isWhitespace(string.charAt(length - 1)))
                break;
        }
        return string.substring(0, length);
    }
}
