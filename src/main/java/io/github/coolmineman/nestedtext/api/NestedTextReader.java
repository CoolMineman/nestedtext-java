package io.github.coolmineman.nestedtext.api;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import io.github.coolmineman.nestedtext.api.tree.NestedTextNode;
import io.github.coolmineman.nestedtext.impl.CommentLine;
import io.github.coolmineman.nestedtext.impl.KeyLine;
import io.github.coolmineman.nestedtext.impl.KeyWithLeafLine;
import io.github.coolmineman.nestedtext.impl.Line;
import io.github.coolmineman.nestedtext.impl.ListItemLine;
import io.github.coolmineman.nestedtext.impl.ListItemWithLeafLine;
import io.github.coolmineman.nestedtext.impl.NestedTextBranch;
import io.github.coolmineman.nestedtext.impl.NestedTextLeaf;
import io.github.coolmineman.nestedtext.impl.NestedTextLexer;
import io.github.coolmineman.nestedtext.impl.StringLine;

public final class NestedTextReader {
    private NestedTextReader() { }

    public static NestedTextNode read(InputStream inputStream) throws NestedTextParseException {
        return read(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
    }

    public static NestedTextNode read(Reader reader) throws NestedTextParseException {
        return read(new BufferedReader(reader));
    }

    public static NestedTextNode read(BufferedReader reader) throws NestedTextParseException {
        int lineNumber = 1;
        StringBuilder lineBuilder = new StringBuilder();
        List<Line> lines = new ArrayList<>();
        try {
            int current = reader.read();
            String line;

            while (current != -1) {
                // NestedText ignores empty lines special handling for \n\r is not needed
                if (current == '\n' || current == '\r') {
                    line = lineBuilder.toString();
                    if (!isBlankLine(line)) {
                        Line line1 = NestedTextLexer.lexLine(line, lineNumber);
                        if (!(line1 instanceof CommentLine)) lines.add(line1); // Ignore Comments (For Now)
                    }
                    lineBuilder.setLength(0); // Clear String Buffer
                    lineNumber++;
                } else {
                    lineBuilder.appendCodePoint(current);
                }
                current = reader.read();
            }
            line = lineBuilder.toString();
            if (!isBlankLine(line)) {
                Line line1 = NestedTextLexer.lexLine(line, lineNumber);
                if (!(line1 instanceof CommentLine)) lines.add(line1); // Ignore Comments (For Now)
            }
            return parseLines(lines);
        } catch (Exception e) {
            throw new NestedTextParseException(e);
        }
    }

    private static NestedTextNode parseLines(List<Line> lines) {
        NestedTextBranch root = new NestedTextBranch();
        List<NestedTextBranch> stack = new ArrayList<>();
        stack.add(root);
        List<Integer> indentSizes = new ArrayList<>();
        int expectedIndent = 0;
        boolean expectedIndentPositive = false;
        for (int i = 0; i < lines.size(); i++) {
            Line line = lines.get(i);
            if (!(line instanceof CommentLine)) {
                if (expectedIndentPositive && (indentSizes.isEmpty() ? line.indentSpaces > 0 : line.indentSpaces > indentSizes.get(indentSizes.size() - 1))) {
                    expectedIndentPositive = false;
                    expectedIndent = line.indentSpaces;
                    indentSizes.add(line.indentSpaces - sum(indentSizes));
                }

                while (line.indentSpaces < expectedIndent || (expectedIndentPositive && line.indentSpaces == expectedIndent)) {
                    if (!expectedIndentPositive && !indentSizes.isEmpty()) expectedIndent -= indentSizes.remove(indentSizes.size() - 1);
                    expectedIndentPositive = false;
                    stack.remove(stack.size() - 1);
                }

                if (line instanceof KeyLine) {
                    NestedTextBranch branch = stack.get(stack.size() - 1);
                    if (line instanceof KeyWithLeafLine) {
                        KeyWithLeafLine keyWithLeafLine = (KeyWithLeafLine)line;
                        branch.asMap().put(keyWithLeafLine.key, new NestedTextLeaf(keyWithLeafLine.leaf));
                    } else {
                        KeyLine keyLine = (KeyLine)line;
                        if (i + 1 <= lines.size() - 1 && lines.get(i + 1) instanceof StringLine) {
                            StringBuilder leafBuilder = new StringBuilder();
                            int j = i + 1;
                            Line line2;
                            line2 = lines.get(j);
                            int expectedIndent2 = line2.indentSpaces;
                            while ((line2 instanceof StringLine) && line2.indentSpaces == expectedIndent2) {
                                leafBuilder.append(((StringLine)line2).string);
                                leafBuilder.append('\n');
                                j++;
                                if (j >= lines.size()) {
                                    break;
                                }
                                line2 = lines.get(j);
                            }
                            i = j - 1;
                            leafBuilder.setLength(leafBuilder.length() - 1); //Remove Last Newline
                            branch.asMap().put(keyLine.key, new NestedTextLeaf(leafBuilder.toString()));
                        } else {
                            NestedTextBranch newBranch = new NestedTextBranch();
                            stack.get(stack.size() - 1).asMap().put(((KeyLine)line).key, newBranch);
                            stack.add(newBranch);
                            expectedIndentPositive = true;
                        }
                    }
                } else if (line instanceof ListItemLine) {
                    NestedTextBranch branch = stack.get(stack.size() - 1);
                    if (line instanceof ListItemWithLeafLine) {
                        ListItemWithLeafLine listItemWithLeafLine = (ListItemWithLeafLine)line;
                        branch.asList().add(new NestedTextLeaf(listItemWithLeafLine.leaf));
                    } else {
                        if (lines.size() - 1 >= i + 1 && lines.get(i + 1) instanceof StringLine) {
                            StringBuilder leafBuilder = new StringBuilder();
                            int j = i + 1;
                            Line line2;
                            line2 = lines.get(j);
                            int expectedIndent2 = line2.indentSpaces;
                            while (line2 instanceof StringLine && line2.indentSpaces == expectedIndent2) {
                                leafBuilder.append(((StringLine)line2).string);
                                leafBuilder.append('\n');
                                j++;
                                if (j >= lines.size()) {
                                    break;
                                }
                                line2 = lines.get(j);
                            }
                            i = j - 1;
                            leafBuilder.setLength(leafBuilder.length() - 1); //Remove Last Newline
                            branch.asList().add(new NestedTextLeaf(leafBuilder.toString()));
                        } else {
                            NestedTextBranch newBranch = new NestedTextBranch();
                            stack.get(stack.size() - 1).asList().add(newBranch);
                            stack.add(newBranch);
                            expectedIndentPositive = true;
                        }
                    }
                } else if (line instanceof StringLine) {
                    if (root.isList() || root.isMap()) {
                        throw new NestedTextParseException(i, 0, "Unclaimed String");
                    }
                    StringBuilder returnStringBuilder = new StringBuilder();
                    for (; i < lines.size(); i++) {
                        StringLine stringLine = (StringLine) lines.get(i);
                        if (stringLine.indentSpaces != line.indentSpaces) {
                            throw new NestedTextParseException(i, 0, "Illegal Indent");
                        }
                        returnStringBuilder.append(stringLine.string);
                        returnStringBuilder.append('\n');
                    }
                    returnStringBuilder.setLength(returnStringBuilder.length() - 1); // Remove Last Newline
                    return NestedTextNode.of(returnStringBuilder.toString());
                }
            }
        }
        return root;
    }

    private static int sum(List<Integer> list) {
        int result = 0;
        for (int i = 0; i < list.size(); i++) {
            result += list.get(i);
        }
        return result;
    }

    private static boolean isBlankLine(String string) {
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            if (!(c == ' ' || c == '\t')) return false;
        }
        return true;
    }
}
