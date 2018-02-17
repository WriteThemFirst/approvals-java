package org.approvalsj.util;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class StackUtilsTest {
    @Test
    void methodNameShouldBeEmpty() {
        StackUtils utils = new StackUtils(String.class);
        assertThat(utils.methodName()).isEmpty();
    }

    @Test
    void methodNameShouldBeTheMethodName() {
        StackUtils utils = new StackUtils(getClass());
        assertThat(utils.methodName()).contains("methodNameShouldBeTheMethodName");
    }

    @Test
    void methodNameShouldNotBeLambda() {
        StackUtils utils = new StackUtils(getClass());
        Stream.of("whatever").forEach(s ->
                assertThat(utils.methodName()).contains("methodNameShouldNotBeLambda"));

    }
}