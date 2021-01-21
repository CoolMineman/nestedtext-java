package io.github.coolmineman.nestedtext.api.tree;

import java.util.List;
import java.util.Map;

public abstract class NestedTextNode {

    /* Package Private */ NestedTextNode() {
    }

    public abstract boolean isLeaf();

    public abstract String asLeafString();

    public abstract boolean isMap();

    public abstract Map<String, NestedTextNode> asMap();

    public abstract boolean isList();

    public abstract List<NestedTextNode> asList();

}
