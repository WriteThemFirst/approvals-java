package com.github.writethemfirst.approvals.reporter;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.writethemfirst.approvals.Approvals;
import com.github.writethemfirst.approvals.util.TestClassCompanion;
import org.junit.jupiter.api.Test;

class JUnit5ReporterTest {
    private Approvals approvals = new Approvals(getClass(), new JUnit5Reporter());
    private TestClassCompanion testClassCompanion = new TestClassCompanion(getClass());


    @Test
    void shouldThrowWhenMismatch() {
        testClassCompanion.writeApproved("some text");

        assertThatThrownBy(() -> approvals.verify("other text"))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("expected: <some text> but was: <other text>");

        testClassCompanion.removeApproved();
        testClassCompanion.removeReceived();

    }


    @Test
    void shouldThrowWhenMissing() {
        testClassCompanion.removeApproved();

        assertThatThrownBy(() -> approvals.verify("text"))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("expected: <> but was: <text>");

        testClassCompanion.removeReceived();

    }
}