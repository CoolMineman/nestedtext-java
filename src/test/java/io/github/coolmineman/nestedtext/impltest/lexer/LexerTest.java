package io.github.coolmineman.nestedtext.impltest.lexer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import io.github.coolmineman.nestedtext.impl.CommentLine;
import io.github.coolmineman.nestedtext.impl.KeyLine;
import io.github.coolmineman.nestedtext.impl.KeyWithLeafLine;
import io.github.coolmineman.nestedtext.impl.ListItemLine;
import io.github.coolmineman.nestedtext.impl.ListItemWithLeafLine;
import io.github.coolmineman.nestedtext.impl.NestedTextLexer;
import io.github.coolmineman.nestedtext.impl.StringLine;

public class LexerTest {
    @Test
    void commentTest() {
        CommentLine line = (CommentLine) NestedTextLexer.lexLine("  #test", 1);
        assertEquals("test", line.comment);
        CommentLine line2 = (CommentLine) NestedTextLexer.lexLine("#test2", 2);
        assertEquals("test2", line2.comment);
        CommentLine line3 = (CommentLine) NestedTextLexer.lexLine("       #test3", 3);
        assertEquals("test3", line3.comment);
    }

    @Test
    void keyTest1() {
        KeyLine line = (KeyLine) NestedTextLexer.lexLine("vice president:", 1);
        assertEquals("vice president", line.key);
        assertEquals(0, line.indentSpaces);
        KeyLine line2 = (KeyLine) NestedTextLexer.lexLine("    address:", 2);
        assertEquals("address", line2.key);
        assertEquals(4, line2.indentSpaces);
    }

    @Test
    void keyTest2() {
        KeyWithLeafLine line = (KeyWithLeafLine) NestedTextLexer.lexLine("    email: cool@example.com", 1);
        assertEquals(4, line.indentSpaces);
        assertEquals("email", line.key);
        assertEquals("cool@example.com", line.leaf);
        KeyWithLeafLine line2 = (KeyWithLeafLine) NestedTextLexer.lexLine("     key 4.1: value 4.1", 2);
        assertEquals(5, line2.indentSpaces);
        assertEquals("key 4.1", line2.key);
        assertEquals("value 4.1", line2.leaf);
        KeyWithLeafLine line3 = (KeyWithLeafLine) NestedTextLexer.lexLine(":-#:'>: :-#:\">:", 3);
        assertEquals(0, line3.indentSpaces);
        assertEquals(":-#:'>", line3.key);
        assertEquals(":-#:\">:", line3.leaf);
        KeyWithLeafLine line4 = (KeyWithLeafLine) NestedTextLexer.lexLine("-#:'>: -#:\">:", 3);
        assertEquals("-#:'>", line4.key);
        assertEquals("-#:\">:", line4.leaf);
        assertEquals(0, line4.indentSpaces);
    }

    @Test
    void keyTest3() {
        KeyWithLeafLine line = (KeyWithLeafLine) NestedTextLexer.lexLine("\" ' key9 ' \": value 9", 1);
        assertEquals(0, line.indentSpaces);
        assertEquals(" ' key9 ' ", line.key);
        assertEquals("value 9", line.leaf);
        KeyWithLeafLine line2 = (KeyWithLeafLine) NestedTextLexer.lexLine("  '- key 3': - value 3", 2);
        assertEquals(2, line2.indentSpaces);
        assertEquals("- key 3", line2.key);
        assertEquals("- value 3", line2.leaf);
    }

    @Test
    void listItemTest1() {
        ListItemLine line = (ListItemLine) NestedTextLexer.lexLine("    -", 1);
        assertEquals(4, line.indentSpaces);
        ListItemLine line2 = (ListItemLine) NestedTextLexer.lexLine("  -", 2);
        assertEquals(2, line2.indentSpaces);
    }

    @Test
    void listItemTest2() {
        ListItemWithLeafLine line = (ListItemWithLeafLine) NestedTextLexer.lexLine("        - board member", 1);
        assertEquals(8, line.indentSpaces);
        assertEquals("board member", line.leaf);
        ListItemWithLeafLine line2 = (ListItemWithLeafLine) NestedTextLexer.lexLine("- And the winner is: {winner}", 2);
        assertEquals(0, line2.indentSpaces);
        assertEquals("And the winner is: {winner}", line2.leaf);
    }

    @Test
    void stringTest() {
        StringLine line = (StringLine) NestedTextLexer.lexLine("> what makes it green?", 1);
        assertEquals(0, line.indentSpaces);
        assertEquals("what makes it green?", line.string);
        StringLine line2 = (StringLine) NestedTextLexer.lexLine("    > This is a multiline string.  It should end without a newline.", 2);
        assertEquals(4, line2.indentSpaces);
        assertEquals("This is a multiline string.  It should end without a newline.", line2.string);
        StringLine line3 = (StringLine) NestedTextLexer.lexLine(">", 3);
        assertEquals(0, line3.indentSpaces);
        assertEquals("", line3.string);
    }

    @Test
    void edgeCase() {
        String lineText = "\" \" key13: \" : value 13";
        KeyWithLeafLine line = (KeyWithLeafLine) NestedTextLexer.lexLine(lineText, 1);
        assertEquals(0, line.indentSpaces);
        assertEquals(" \" key13: ", line.key);
        assertEquals("value 13", line.leaf);
    }

    @Test
    void edgetCase2() {
        String lineText = "' \" key19'\" : ' : value 19";
        KeyWithLeafLine line = (KeyWithLeafLine) NestedTextLexer.lexLine(lineText, 1);
        assertEquals(0, line.indentSpaces);
        assertEquals(" \" key19'\" : ", line.key);
        assertEquals("value 19", line.leaf);
    }
}
