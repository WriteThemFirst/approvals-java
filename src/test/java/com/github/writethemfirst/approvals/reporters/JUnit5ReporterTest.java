package com.github.writethemfirst.approvals.reporters;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.writethemfirst.approvals.Approvals;
import com.github.writethemfirst.approvals.ApprovalsFiles;
import org.junit.jupiter.api.Test;

class JUnit5ReporterTest {
    private Approvals approvals = new Approvals(new JUnit5Reporter());
    private ApprovalsFiles approvalsFiles = new ApprovalsFiles(getClass());


    @Test
    void shouldThrowWhenMismatch() {
        approvalsFiles.writeApproved("some text");

        assertThatThrownBy(() -> approvals.verify("other text"))
            .isInstanceOf(AssertionError.class)
            .hasMessageContaining("expected: <some text> but was: <other text>");

        approvalsFiles.removeApproved();
        approvalsFiles.removeReceived();

    }


    @Test
    void shouldThrowWhenMissing() {
        approvalsFiles.removeApproved();

        assertThatThrownBy(() -> approvals.verify("text"))
            .isInstanceOf(AssertionError.class)
            .hasMessageContaining("expected: <> but was: <text>");

        approvalsFiles.removeReceived();

    }
}
