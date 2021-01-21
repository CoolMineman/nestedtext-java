package io.github.coolmineman.nestedtext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileInputStream;
import java.io.StringReader;

import com.google.gson.JsonElement;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import io.github.coolmineman.nestedtext.api.NestedTextReader;
import io.github.coolmineman.nestedtext.api.tree.NestedTextNode;
import io.github.coolmineman.nestedtext.util.JsonUtil;

public class ReaderTest {
    @Test
    void readerTest1() {
        NestedTextNode branch = NestedTextReader.read(new StringReader(
                "vice president:\n    name: Margaret Hodge\n    address:\n        > 2586 Marigold Lane\n        > Topeka, Kansas 20682"));
        assertEquals(true, branch.asMap().containsKey("vice president"));
    }

    @Test
    void dict_01() {
        NestedTextNode branch = NestedTextReader.read(new StringReader("key1:\nkey2:\n"));
        assertTrue(branch.asMap().containsKey("key1"));
        assertTrue(branch.asMap().containsKey("key2"));
    }

    @Test
    void edgeCase1() { //No Newline
        NestedTextNode node = NestedTextReader.read(new StringReader("-\n\n-  — "));
        assertEquals("", node.asList().get(0).asLeafString());
        assertEquals(" — ", node.asList().get(1).asLeafString());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7})
    void holistic(int a) throws Exception {
        String dir = "nestedtext_tests/test_cases/holistic_" + a + "/";
        FileInputStream ntFile = new FileInputStream(dir + "load_in.nt");
        FileInputStream jsonFile = new FileInputStream(dir + "load_out.json");
        NestedTextNode node = NestedTextReader.read(ntFile);
        JsonElement jsonElement = JsonUtil.readJson(jsonFile);
        JsonUtil.assertNtEqualsJson(node, jsonElement, "File " + a);
    }

    @ParameterizedTest
    @ValueSource(strings = {"01", "16", "17", "18", "19", "20", "23"})
    void dict(String a) throws Exception {
        String dir = "nestedtext_tests/test_cases/dict_" + a + "/";
        FileInputStream ntFile = new FileInputStream(dir + "load_in.nt");
        FileInputStream jsonFile = new FileInputStream(dir + "load_out.json");
        NestedTextNode node = NestedTextReader.read(ntFile);
        JsonElement jsonElement = JsonUtil.readJson(jsonFile);
        JsonUtil.assertNtEqualsJson(node, jsonElement, "File " + a);
    }
}
