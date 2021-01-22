package io.github.coolmineman.nestedtext.impl;

import java.util.List;
import java.util.Map;

import io.github.coolmineman.nestedtext.api.tree.NestedTextNode;

public final class NestedTextLeaf implements NestedTextNode {
    private final String leaf;
    private String comment = null;

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
        return 0; //TODO old hashcode was broken
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        if (!(obj instanceof NestedTextNode)) {
            return false;
        }
        NestedTextNode other = (NestedTextNode) obj;
        if (!other.isLeaf()) {
            return false;
        }
        if (other.isMap() || other.isList()) {
            return false;
        }
        return this.asLeafString().equals(other.asLeafString());
    }

    @Override
    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String getComment() {
        return comment;
    }
    
}
