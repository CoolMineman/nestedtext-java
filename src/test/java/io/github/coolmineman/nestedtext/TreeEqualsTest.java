package io.github.coolmineman.nestedtext;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import io.github.coolmineman.nestedtext.api.tree.NestedTextNode;
import io.github.coolmineman.nestedtext.impl.NestedTextBranch;
import io.github.coolmineman.nestedtext.impl.NestedTextLeaf;

class TreeEqualsTest {
    @Test
    void test() {
        assertEquals((NestedTextNode) new NestedTextBranch(), (NestedTextNode) new NestedTextLeaf(""));
    }
}
