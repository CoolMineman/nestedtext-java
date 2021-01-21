package io.github.coolmineman.nestedtext.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.coolmineman.nestedtext.api.NestedTextParseException;
import io.github.coolmineman.nestedtext.api.TabException;

public class NestedTextLexer {
    private NestedTextLexer() { }

    public static Line lexLine(String line, int lineNumber) {
        int indent = 0;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            boolean isLastChar = i == line.length() - 1;
            switch (c) {
                case ' ':
                    indent++;
                    break;
                case '\t':
                    throw new TabException();
                case '#':
                    CommentLine result = new CommentLine();
                    result.indentSpaces = -1; //Indentation For Comments Doesn't Matter
                    result.comment = line.substring(i + 1);
                    return result;
                case '"':
                    Matcher matcher = Pattern.compile("(\"\\s*: )").matcher(line).region(i + 1, line.length() - 1);
                    if (!matcher.find() && line.charAt(line.length() - 1) == ':' && line.charAt(line.length() - 2) == '"') {
                        KeyLine result4 = new KeyLine();
                        result4.indentSpaces = indent;
                        result4.key = line.substring(i + 1, line.length() - 2);
                        return result4;
                    } else {
                        KeyWithLeafLine result5 = new KeyWithLeafLine();
                        result5.indentSpaces = indent;
                        result5.key = line.substring(i + 1, matcher.start(1));
                        result5.leaf = line.substring(matcher.end(1));
                        return result5;
                    }
                case '\'':
                    Matcher matcher2 = Pattern.compile("('\\s*: )").matcher(line).region(i + 1, line.length() - 1);
                    if (!matcher2.find() && line.charAt(line.length() - 1) == ':' && line.charAt(line.length() - 2) == '\'') {
                        KeyLine result6 = new KeyLine();
                        result6.indentSpaces = indent;
                        result6.key = line.substring(i + 1, line.length() - 2);
                        return result6;
                    } else {
                        KeyWithLeafLine result7 = new KeyWithLeafLine();
                        result7.indentSpaces = indent;
                        result7.key = line.substring(i + 1, matcher2.start(1));
                        result7.leaf = line.substring(matcher2.end(1));
                        return result7;
                    }
                case '-':
                    if (isLastChar) {
                        ListItemLine result1 = new ListItemLine();
                        result1.indentSpaces = indent;
                        return result1;
                    } else {
                        if (line.charAt(i + 1) == ' ') {
                            if (line.length() - 1 >= i + 2) {
                                ListItemWithLeafLine result2 = new ListItemWithLeafLine();
                                result2.indentSpaces = indent;
                                result2.leaf = line.substring(i + 2);
                                return result2;
                            } else {
                                ListItemWithLeafLine result2 = new ListItemWithLeafLine();
                                result2.indentSpaces = indent;
                                result2.leaf = "";
                                return result2;
                            }
                        }
                        //Else attempt as key
                        return defaultCase(i, indent, line, lineNumber);
                    }
                case '>':
                    StringLine result3 = new StringLine();
                    result3.indentSpaces = indent;
                    if (isLastChar) {
                        result3.string = "";
                        return result3;
                    } else {
                        if (line.charAt(i + 1) == ' ') {
                            result3.string = line.substring(i + 2);
                            return result3;
                        }
                        //Else attempt as key
                        return defaultCase(i, indent, line, lineNumber);
                    }
                default:
                    return defaultCase(i, indent, line, lineNumber);
            }
        }
        throw new UnsupportedOperationException("Cannot Lex Empty Line");
    }

    private static Line defaultCase(int i, int indent, String line, int lineNumber) {
        int colonSpaceIndex = line.indexOf(": ");
        if (colonSpaceIndex == -1 && line.charAt(line.length() - 1) == ':') {
            KeyLine result0 = new KeyLine();
            result0.indentSpaces = indent;
            result0.key = line.substring(i, line.length() - 1).stripTrailing(); //substring ends at endIndex - 1
            return result0;
        } else {
            KeyWithLeafLine result00 = new KeyWithLeafLine();
            result00.indentSpaces = indent;
            if (colonSpaceIndex == -1) throw new NestedTextParseException(lineNumber, 0, "Invalid Line (Comment Should Start With #)");
            result00.key = line.substring(i, colonSpaceIndex).stripTrailing();
            result00.leaf = line.substring(colonSpaceIndex + 2);
            return result00;
        }
    }
}
