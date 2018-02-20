package org.approvalsj.reporter;

import org.approvalsj.Approvals;
import org.approvalsj.util.FileUtils;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JUnit5ReporterTest {
    private Approvals approvals = new Approvals(getClass(), new JUnit5Reporter());
    private FileUtils fileUtils = new FileUtils(getClass());


    @Test
    void shouldThrowWhenMismatch() {
        fileUtils.writeApproved("some text");

        assertThatThrownBy(() -> approvals.verify("other text"))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("expected: <some text> but was: <other text>");

        fileUtils.removeApproved();
        fileUtils.removeReceived();

    }

    @Test
    void shouldThrowWhenMissing() {
        fileUtils.removeApproved();

        assertThatThrownBy(() -> approvals.verify("text"))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("expected: <> but was: <text>");

        fileUtils.removeReceived();

    }
}