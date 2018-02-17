package org.approvalsj.util;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StackUtilsTest {
    @Test
    void methodNameShouldBeEmpty() {
        StackUtils utils = new StackUtils(String.class);
        assertEquals(Optional.empty(), utils.methodName());
    }

    @Test
    void methodNameShouldBeTheMethodName() {
        StackUtils utils = new StackUtils(getClass());
        assertEquals(Optional.of("methodNameShouldBeTheMethodName"), utils.methodName());
    }
}