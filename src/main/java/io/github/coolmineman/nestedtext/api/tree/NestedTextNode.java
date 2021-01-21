package io.github.coolmineman.nestedtext.api.tree;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import io.github.coolmineman.nestedtext.impl.NestedTextBranch;
import io.github.coolmineman.nestedtext.impl.NestedTextLeaf;

public interface NestedTextNode {

    boolean isLeaf();

    String asLeafString();

    boolean isMap();

    Map<String, NestedTextNode> asMap();

    boolean isList();

    List<NestedTextNode> asList();

    public static NestedTextNode of(String string) {
        return new NestedTextLeaf(string);
    }

    public static NestedTextNode of(Map<String, NestedTextNode> dict) {
        return new NestedTextBranch(dict, Collections.emptyList());
    }

    public static NestedTextNode of(List<NestedTextNode> list) {
        return new NestedTextBranch(Collections.emptyMap(), list);
    }

}
