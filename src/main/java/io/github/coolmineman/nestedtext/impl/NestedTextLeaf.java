package io.github.coolmineman.nestedtext.impl;

import java.util.List;
import java.util.Map;

import io.github.coolmineman.nestedtext.api.tree.NestedTextNode;

public final class NestedTextLeaf implements NestedTextNode {
    private final String leaf;

    public NestedTextLeaf(String leaf) {
        this.leaf = leaf;
    }

    @Override
    public boolean isLeaf() {
        return true;
    }

    @Override
    public String asLeafString() {
        return leaf;
    }

    @Override
    public boolean isMap() {
        return false;
    }

    @Override
    public Map<String, NestedTextNode> asMap() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isList() {
        return false;
    }

    @Override
    public List<NestedTextNode> asList() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int hashCode() {
        return leaf.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass())
            return false;
        return leaf.equals(((NestedTextLeaf)obj).asLeafString());
    }
    
}
