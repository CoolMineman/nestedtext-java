package io.github.coolmineman.nestedtext.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.coolmineman.nestedtext.api.tree.NestedTextNode;

public final class NestedTextBranch implements NestedTextNode {

    private final Map<String, NestedTextNode> dict;
    private final List<NestedTextNode> list;

    public NestedTextBranch(Map<String, NestedTextNode> dict, List<NestedTextNode> list) {
        super();
        this.dict = dict;
        this.list = list;
    }

    public NestedTextBranch() {
        this(new HashMap<>(), new ArrayList<>());
    }

    @Override
    public boolean isLeaf() {
        return !isMap() && !isList(); // In Nested Text Empty Maps/Lists Are Empty Strings
    }

    @Override
    public String asLeafString() {
        if (isLeaf()) return "";
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isMap() {
        return !dict.isEmpty();
    }

    @Override
    public Map<String, NestedTextNode> asMap() {
        return dict;
    }

    @Override
    public boolean isList() {
        return !list.isEmpty();
    }

    @Override
    public List<NestedTextNode> asList() {
        return list;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + dict.hashCode();
        result = 31 * result + list.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass())
            return false;
        NestedTextBranch other = (NestedTextBranch) obj;
        return asMap().equals(other.asMap()) && asList().equals(other.asList());
    }
}
