package io.github.coolmineman.nestedtext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileInputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import io.github.coolmineman.nestedtext.api.NestedTextReader;
import io.github.coolmineman.nestedtext.api.NestedTextWriter;
import io.github.coolmineman.nestedtext.api.tree.NestedTextNode;

class WriterTest {
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7})
    void holistic(int a) throws Exception {
        String dir = "nestedtext_tests/test_cases/holistic_" + a + "/";
        FileInputStream ntFile = new FileInputStream(dir + "load_in.nt");
        NestedTextNode node = NestedTextReader.read(ntFile);
        StringWriter writer = new StringWriter();
        NestedTextWriter.write(node, writer);
        NestedTextNode node2 = NestedTextReader.read(new StringReader(writer.toString()));
        assertEquals(node, node2, Integer.toString(a));
    }

    @ParameterizedTest
    @ValueSource(strings = {"01", "16", "17", "18", "19", "20", "23"})
    void dict(String a) throws Exception {
        String dir = "nestedtext_tests/test_cases/dict_" + a + "/";
        FileInputStream ntFile = new FileInputStream(dir + "load_in.nt");
        NestedTextNode node = NestedTextReader.read(ntFile);
        StringWriter writer = new StringWriter();
        NestedTextWriter.write(node, writer);
        NestedTextNode node2 = NestedTextReader.read(new StringReader(writer.toString()));
        assertEquals(node, node2, a);
    }

    @Test
    void comment() {
        NestedTextNode node = NestedTextNode.of(new HashMap<>());
        node.setComment("hahaha comment go brr");
        StringWriter writer = new StringWriter();
        NestedTextWriter.write(node, writer);
        assertEquals("# hahaha comment go brr", writer.toString().split("\\n")[0]);
    }

    @Test
    void edgeCase() {
        String annoyingString = " \" key19'\" :  ";
        NestedTextNode node = NestedTextNode.of(Collections.singletonMap(annoyingString, NestedTextNode.of("ahhhhhhh")));
        StringWriter writer = new StringWriter();
        NestedTextWriter.write(node, writer);
        NestedTextNode node2 = NestedTextReader.read(new StringReader(writer.toString()));
        assertEquals(node, node2);
    }

    @Test
    void edgeCase2() {
        String ir = "': key 7': : value 7";
        NestedTextNode node = NestedTextReader.read(new StringReader(ir));
        StringWriter writer = new StringWriter();
        NestedTextWriter.write(node, writer);
        NestedTextNode node2 = NestedTextReader.read(new StringReader(writer.toString()));
        assertEquals(node, node2);
    }
}
