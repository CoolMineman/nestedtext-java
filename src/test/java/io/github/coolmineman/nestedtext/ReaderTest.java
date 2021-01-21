package io.github.coolmineman.nestedtext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileInputStream;
import java.io.StringReader;

import com.google.gson.JsonElement;

import org.junit.jupiter.api.Test;

import io.github.coolmineman.nestedtext.api.NestedTextReader;
import io.github.coolmineman.nestedtext.api.tree.NestedTextBranch;
import io.github.coolmineman.nestedtext.api.tree.NestedTextNode;
import io.github.coolmineman.nestedtext.util.JsonUtil;

public class ReaderTest {
    @Test
    void readerTest1() {
        NestedTextBranch branch = (NestedTextBranch) NestedTextReader.read(new StringReader(
                "vice president:\n    name: Margaret Hodge\n    address:\n        > 2586 Marigold Lane\n        > Topeka, Kansas 20682"));
        assertEquals(true, branch.asMap().containsKey("vice president"));
    }

    @Test
    void dict_01() {
        NestedTextBranch branch = (NestedTextBranch) NestedTextReader.read(new StringReader("key1:\nkey2:\n"));
        assertTrue(branch.asMap().containsKey("key1"));
        assertTrue(branch.asMap().containsKey("key2"));
    }

    @Test
    void edgeCase1() { //No Newline
        NestedTextNode node = NestedTextReader.read(new StringReader("-\n\n-  — "));
        assertEquals("", node.asList().get(0).asLeafString());
        assertEquals(" — ", node.asList().get(1).asLeafString());
    }

    @Test
    void holistic_1() throws Exception {
        FileInputStream ntFile = new FileInputStream("nestedtext_tests/test_cases/holistic_1/load_in.nt");
        FileInputStream jsonFile = new FileInputStream("nestedtext_tests/test_cases/holistic_1/load_out.json");
        NestedTextNode node = NestedTextReader.read(ntFile);
        JsonElement jsonElement = JsonUtil.readJson(jsonFile);
        JsonUtil.assertNtEqualsJson(node, jsonElement);
    }

    @Test
    void holistic_2() throws Exception {
        FileInputStream ntFile = new FileInputStream("nestedtext_tests/test_cases/holistic_2/load_in.nt");
        FileInputStream jsonFile = new FileInputStream("nestedtext_tests/test_cases/holistic_2/load_out.json");
        NestedTextNode node = NestedTextReader.read(ntFile);
        JsonElement jsonElement = JsonUtil.readJson(jsonFile);
        JsonUtil.assertNtEqualsJson(node, jsonElement);
    }

    @Test
    void holistic_3() throws Exception {
        FileInputStream ntFile = new FileInputStream("nestedtext_tests/test_cases/holistic_3/load_in.nt");
        FileInputStream jsonFile = new FileInputStream("nestedtext_tests/test_cases/holistic_3/load_out.json");
        NestedTextNode node = NestedTextReader.read(ntFile);
        JsonElement jsonElement = JsonUtil.readJson(jsonFile);
        JsonUtil.assertNtEqualsJson(node, jsonElement);
    }

    @Test
    void holistic_4() throws Exception {
        FileInputStream ntFile = new FileInputStream("nestedtext_tests/test_cases/holistic_4/load_in.nt");
        FileInputStream jsonFile = new FileInputStream("nestedtext_tests/test_cases/holistic_4/load_out.json");
        NestedTextNode node = NestedTextReader.read(ntFile);
        JsonElement jsonElement = JsonUtil.readJson(jsonFile);
        JsonUtil.assertNtEqualsJson(node, jsonElement);
    }

    @Test
    void holistic_5() throws Exception {
        FileInputStream ntFile = new FileInputStream("nestedtext_tests/test_cases/holistic_5/load_in.nt");
        FileInputStream jsonFile = new FileInputStream("nestedtext_tests/test_cases/holistic_5/load_out.json");
        NestedTextNode node = NestedTextReader.read(ntFile);
        JsonElement jsonElement = JsonUtil.readJson(jsonFile);
        JsonUtil.assertNtEqualsJson(node, jsonElement);
    }

    @Test
    void holistic_6() throws Exception {
        FileInputStream ntFile = new FileInputStream("nestedtext_tests/test_cases/holistic_6/load_in.nt");
        FileInputStream jsonFile = new FileInputStream("nestedtext_tests/test_cases/holistic_6/load_out.json");
        NestedTextNode node = NestedTextReader.read(ntFile);
        JsonElement jsonElement = JsonUtil.readJson(jsonFile);
        JsonUtil.assertNtEqualsJson(node, jsonElement);
    }

    @Test
    void holistic_7() throws Exception {
        FileInputStream ntFile = new FileInputStream("nestedtext_tests/test_cases/holistic_7/load_in.nt");
        FileInputStream jsonFile = new FileInputStream("nestedtext_tests/test_cases/holistic_7/load_out.json");
        NestedTextNode node = NestedTextReader.read(ntFile);
        JsonElement jsonElement = JsonUtil.readJson(jsonFile);
        JsonUtil.assertNtEqualsJson(node, jsonElement);
    }
}
