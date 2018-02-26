package com.github.writethemfirst.approvals;

import com.github.writethemfirst.approvals.reporters.ThrowsReporter;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ApprovalsSimpleTest {
    private Approvals approvals = new Approvals(new ThrowsReporter());
    private ApprovalsFiles approvalsFiles = new ApprovalsFiles();


    @Test
    void shouldDoNothingWhenApprovedFileExistsAndIsCorrect() {
        approvalsFiles.writeApproved("some text");
        approvals.verify("some text");
        approvalsFiles.removeApproved();
    }


    @Test
    void shouldFailWhenApprovedFileExistsAndIsDifferent() {
        approvalsFiles.writeApproved("expected text");

        assertThatThrownBy(() -> approvals.verify("actual text"))
            .isInstanceOf(AssertionError.class)
            .hasMessage("expected: <expected text> but was: <actual text>");

        approvalsFiles.removeApproved();
        approvalsFiles.removeReceived();
    }


    @Test
    void shouldFailWhenApprovedFileDoesNotExist() {
        approvalsFiles.removeApproved();

        assertThatThrownBy(() -> approvals.verify("text"))
            .isInstanceOf(AssertionError.class)
            .hasMessageContaining("expected: <> but was: <text>");

        approvalsFiles.removeReceived();
    }


    @Test
    void shouldKeepReceivedFileWhenApprovedFileDoesNotExist() {
        approvalsFiles.removeApproved();
        approvalsFiles.removeReceived();
        try {
            approvals.verify("text");
        } catch (AssertionError e) {
            String received = approvalsFiles.readReceived();
            assertThat(received).isEqualTo("text");
        }
        approvalsFiles.removeReceived();
        approvalsFiles.removeApproved();
    }


    @Test
    void shouldKeepReceivedFileWhenApprovedFileMismatch() {
        approvalsFiles.writeApproved("approved");
        try {
            approvals.verify("text");
        } catch (AssertionError e) {
            String received = approvalsFiles.readReceived();
            assertThat(received).isEqualTo("text");
        }
        approvalsFiles.removeReceived();
        approvalsFiles.removeApproved();
    }


    @Test
    void shouldRemoveReceivedFileWhenApprovedFileMatch() {
        approvalsFiles.writeReceived("last content");
        approvalsFiles.writeApproved("same");

        approvals.verify("same");

        String received = approvalsFiles.readReceived();
        assertThat(received).isEqualTo("");

        approvalsFiles.removeApproved();
    }

}
