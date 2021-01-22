package io.github.coolmineman.nestedtext;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import io.github.coolmineman.nestedtext.api.NestedTextValidator;

class ValidatorTest {
    @Test
    void test1() {
        assertNull(NestedTextValidator.validateKey("  g  "));
        assertNotNull(NestedTextValidator.validateKey(" \"   :    \n  \"   : \n"));
    }
}
