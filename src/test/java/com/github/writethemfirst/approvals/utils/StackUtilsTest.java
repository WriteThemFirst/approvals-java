package com.github.writethemfirst.approvals.utils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StackUtilsTest {
    @Test
    void guessedClassShouldBeTestClass() {
        Class<?> guessed = StackUtils.callerClass(StackUtils.class);
        assertThat(guessed).isEqualTo(getClass());
    }
}
