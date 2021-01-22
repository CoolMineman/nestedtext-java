package io.github.coolmineman.nestedtext.api;

import java.util.Map.Entry;

import org.jetbrains.annotations.Nullable;

import io.github.coolmineman.nestedtext.api.tree.NestedTextNode;

public class NestedTextValidator {
    private NestedTextValidator() { }

    /**
     * Validates a node is writeable
     * @param node 
     * @return null if valid message string if invalid
     */
    @Nullable
    public static String validate(NestedTextNode node) {
        if (node.isLeaf() || node.isList()) return null;
        if (node.isMap()) {
            for (Entry<String, NestedTextNode> entry : node.asMap().entrySet()) {
                String key = entry.getKey();
                String keyValid = validateKey(key);
                if (keyValid != null) return keyValid;
                String valueValid = validate(entry.getValue());
                if (valueValid != null) return valueValid;
            }
        }
        return null;
    }

    @Nullable
    public static String validateKey(String key) {
        int quoteWhitespaceColonSpaces = 0;
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            if (c == '\'' || c == '"') {
                for (int j = i + 1; j < key.length(); j++) {
                    char c1 = key.charAt(j);
                    if (c1 == ' ') continue;
                    if (c1 == ':' && key.length() - 1 > j + 1 && key.charAt(j + 1) == ' ') {
                        quoteWhitespaceColonSpaces++;
                        break;
                    } else {
                        break;
                    }
                }
            }
        }
        return quoteWhitespaceColonSpaces >= 2 ? "Invalid Key: " + key : null;
    }
}
