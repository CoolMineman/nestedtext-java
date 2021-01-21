package io.github.coolmineman.nestedtext.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.github.coolmineman.nestedtext.api.tree.NestedTextNode;

public class JsonUtil {
    public static void assertNtEqualsJson(NestedTextNode node, JsonElement jsonElement, String message) {
        if (jsonElement.isJsonArray()) {
            assertTrue(node.isList(), message);
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            List<NestedTextNode> nestedTextNodes = node.asList();
            for (int i = 0; i < jsonArray.size(); i++) {
                assertNtEqualsJson(nestedTextNodes.get(i), jsonArray.get(i), message);
            }
        } else if (jsonElement.isJsonObject()) {
            assertTrue(node.isMap(), message);
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            Map<String, NestedTextNode> map = node.asMap();
            for (Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                Objects.requireNonNull(map.get(entry.getKey()), "Error in " + entry.getKey());
                assertNtEqualsJson(map.get(entry.getKey()), entry.getValue(), message);
            }
        } else {
            assertTrue(node.isLeaf(), message + " Failed For Leaf:" + jsonElement.toString());
            assertEquals(jsonElement.getAsString(), node.asLeafString(), message);
        }
    }

    public static JsonElement readJson(InputStream stream) {
        return JsonParser.parseReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
    }
}
