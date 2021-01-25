package io.github.coolmineman.nestedtext.api;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Map.Entry;

import io.github.coolmineman.nestedtext.api.tree.NestedTextNode;

public class NestedTextWriter {
    private NestedTextWriter() { }

    public static void write(NestedTextNode node, OutputStreamWriter outputStreamWriter) throws NestedTextWriteException {
        write(node, outputStreamWriter, 4);
    }

    public static void write(NestedTextNode node, OutputStream outputStream, int indent) throws NestedTextWriteException {
        write(node, new OutputStreamWriter(outputStream, StandardCharsets.UTF_8), indent);
    }

    public static void write(NestedTextNode node, Writer writer) {
        write(node, writer, 4);
    }

    public static void write(NestedTextNode node, Writer writer, int indent) throws NestedTextWriteException {
        try {
            writeNode(node, writer, 0, indent);
        } catch (NestedTextWriteException e) {
            throw e;
        } catch (Exception e) {
            throw new NestedTextWriteException(e);
        }
    }

    private static void writeNode(NestedTextNode node, Writer writer, int currentIndent, int indent) throws Exception {
        if (node.getComment() != null) {
            indent(currentIndent, writer);
            writer.write("# ");
            writer.write(node.getComment());
            writer.write('\n');
        }
        if (node.isMap()) {
            for (Entry<String, NestedTextNode> entry : node.asMap().entrySet()) {
                if (entry.getValue().getComment() != null) {
                    indent(currentIndent, writer);
                    writer.write("# ");
                    writer.write(entry.getValue().getComment());
                    writer.write('\n');
                }
                indent(currentIndent, writer);
                writer.write(escapeKey(entry.getKey()));
                boolean singleLineString = entry.getValue().isLeaf() && entry.getValue().asLeafString().indexOf('\n') == -1;
                if (singleLineString) {
                    writer.write(": ");
                    writer.write(entry.getValue().asLeafString());
                    writer.write('\n');
                } else {
                    writer.write(":\n");
                    writeNode(entry.getValue(), writer, currentIndent + indent, indent);
                }
            }
        } else if (node.isList()) {
            for (NestedTextNode node2 : node.asList()) {
                if (node2.getComment() != null) {
                    indent(currentIndent, writer);
                    writer.write("# ");
                    writer.write(node2.getComment());
                    writer.write('\n');
                }
                indent(currentIndent, writer);
                boolean singleLineString = node2.isLeaf() && node2.asLeafString().indexOf('\n') == -1;
                if (singleLineString) {
                    writer.write("- ");
                    writer.write(node2.asLeafString());
                    writer.write('\n');
                } else {
                    writer.write("-\n");
                    writeNode(node2, writer, currentIndent + indent, indent);
                }
            }
        } else if (node.isLeaf()) {
            String leaf = node.asLeafString();
            indent(currentIndent, writer);
            writer.write("> ");
            for (int i = 0; i < leaf.length(); i++) {
                char c = leaf.charAt(i);
                if (c == '\n') {
                    writer.write('\n');
                    indent(currentIndent, writer);
                    writer.write("> ");
                } else {
                    writer.append(c);
                }
            }
            writer.write('\n');
        }
    }

    private static String escapeKey(String key) {
        char firstChar = firstNonSpaceChaar(key);
        boolean singleQuote = false;
        boolean doubleQuote = false;
        int quoteWhitespaceColonSpaces = 0;
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            if (c == '\'' || c == '"') {
                for (int j = i + 1; j < key.length(); j++) {
                    char c1 = key.charAt(j);
                    if (c1 == ' ') continue;
                    if (c1 == ':' && key.length() - 1 >= j + 1 && key.charAt(j + 1) == ' ') {
                        if (c == '"') {
                            doubleQuote = true;
                        } else {
                            singleQuote = true;
                        }
                        quoteWhitespaceColonSpaces++;
                        break;
                    } else {
                        break;
                    }
                }
            }
        }

        if (quoteWhitespaceColonSpaces >= 2 || (singleQuote && doubleQuote)) {
            throw new NestedTextWriteException("Invalid Key: " + key);
        }
        if (singleQuote) return "\"" + key + "\"";
        if (doubleQuote) return "\'" + key + "\'";
        if (firstChar == '\'') return "\"" + key + "\"";
        if (firstChar == '\"') return "'" + key + "'";
        if (key.indexOf(": ") != -1) return "\'" + key + "\'";
        if (key.length() > 0 && (key.charAt(0) == ' ' || key.charAt(0) == '-' || key.charAt(0) == '>' || key.charAt(0) == ':' || firstChar == '#')) return "\"" + key + "\"";
        return key;
    }

    private static char firstNonSpaceChaar(String string) {
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == ' ') continue;
            return string.charAt(i);
        }
        return ' ';
    }

    private static void indent(int spaces, Writer writer) throws Exception {
        for (int i = 0; i < spaces; ++i) {
            writer.append(' ');
        }
    }
}
