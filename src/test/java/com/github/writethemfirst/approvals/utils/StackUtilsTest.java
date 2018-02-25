package com.github.writethemfirst.approvals.utils;

import com.github.writethemfirst.approvals.ApprovalsFiles;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static com.github.writethemfirst.approvals.utils.StackUtils.callerMethod;
import static org.assertj.core.api.Assertions.assertThat;

class StackUtilsTest {
    @Test
    void guessedClassShouldBeTestClass() {
        Class<?> guessed = StackUtils.callerClass(StackUtils.class);
        assertThat(guessed).isEqualTo(getClass());
    }

    @Test
    void methodNameShouldBeEmpty() {
        assertThat(callerMethod(String.class)).isEmpty();
    }


    @Test
    void methodNameShouldBeTheMethodName() {
        assertThat(callerMethod(getClass())).contains("methodNameShouldBeTheMethodName");
    }


    @Test
    void methodNameShouldNotBeLambda() {
        Stream.of("whatever")
            .forEach(s -> assertThat(callerMethod(getClass())).contains("methodNameShouldNotBeLambda"));
    }
}
