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
    public static void assertNtEqualsJson(NestedTextNode node, JsonElement jsonElement) {
        if (jsonElement.isJsonArray()) {
            assertTrue(node.isList());
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            List<NestedTextNode> nestedTextNodes = node.asList();
            for (int i = 0; i < jsonArray.size(); i++) {
                assertNtEqualsJson(nestedTextNodes.get(i), jsonArray.get(i));
            }
        } else if (jsonElement.isJsonObject()) {
            assertTrue(node.isMap());
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            Map<String, NestedTextNode> map = node.asMap();
            for (Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                Objects.requireNonNull(map.get(entry.getKey()), "Error in " + entry.getKey());
                assertNtEqualsJson(map.get(entry.getKey()), entry.getValue());
            }
        } else {
            assertTrue(node.isLeaf(), "Failed For Leaf:" + jsonElement.toString());
            assertEquals(jsonElement.getAsString(), node.asLeafString());
        }
    }

    public static JsonElement readJson(InputStream stream) {
        return JsonParser.parseReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
    }
}
