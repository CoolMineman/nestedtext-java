package io.github.coolmineman.nestedtext.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import io.github.coolmineman.nestedtext.api.tree.NestedTextNode;

public final class NestedTextBranch implements NestedTextNode {

    private final Map<String, NestedTextNode> dict;
    private final List<NestedTextNode> list;
    private String comment = null;

    public NestedTextBranch(Map<String, NestedTextNode> dict, List<NestedTextNode> list) {
        super();
        this.dict = dict;
        this.list = list;
    }

    public NestedTextBranch() {
        this(new LinkedHashMap<>(), new ArrayList<>());
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
        if (this.isMap()) {
            if (!other.isMap()) {
                return false;
            }
            if (!mapsEqual(this.asMap(), other.asMap())) {
            // if (!this.asMap().equals(other.asMap())) {
                return false;
            }
        }
        if (this.isList()) {
            if (!other.isList()) {
                return false;
            }
            if (!this.asList().equals(other.asList())) {
                return false;
            }
        }
        if (this.isLeaf()) {
            if (!other.isLeaf()) return false;
            if (!this.asLeafString().equals(other.asLeafString())) {
                return false;
            }
        }
        return true;
    }

    // Debug equals for maps
    private boolean mapsEqual(Map<?, ?> map, Map<?, ?> map2) {
        if (map.size() != map2.size()) {
            System.out.print(map.size());
            System.out.print(map2.size());
            return false;
        }
        for (Entry<?, ?> entry : map.entrySet()) {
            if (!map2.containsKey(entry.getKey())) {
                System.out.println("Bad Key " + entry.getKey());
                return false;
            }
            if (!map2.get(entry.getKey()).equals(entry.getValue())) {
                System.out.println("Bad Value " + entry.getKey());
                return false;
            }
        }
        return true;
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
