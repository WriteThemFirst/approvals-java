package org.approvalsj;

import org.approvalsj.util.TestClassCompanion;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ApprovalsSimpleTest {
    private Approvals approvals = new Approvals(getClass());
    private TestClassCompanion testClassCompanion = new TestClassCompanion(getClass());

    @Test
    void approvalShouldDoNothingWhenApprovedFileExistsAndIsCorrect() throws Throwable {
        testClassCompanion.writeApproved("some text");
        approvals.verify("some text");
        testClassCompanion.removeApproved();
    }

    @Test
    void approvalShouldFailWhenApprovedFileExistsAndIsDifferent() {
        testClassCompanion.writeApproved("expected text");

        assertThatThrownBy(() -> approvals.verify("actual text"))
                .isInstanceOf(AssertionError.class)
                .hasMessage("expected: <expected text> but was: <actual text>");

        testClassCompanion.removeApproved();
        testClassCompanion.removeReceived();
    }

    @Test
    void approvalShouldFailWhenApprovedFileDoesNotExist() {
        testClassCompanion.removeApproved();

        assertThatThrownBy(() -> approvals.verify("text"))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("approvalShouldFailWhenApprovedFileDoesNotExist.approved does not exist yet");

        testClassCompanion.removeReceived();
    }


    @Test
    void approvalShouldKeepReceivedFileWhenApprovedFileDoesNotExist() throws Throwable {
        testClassCompanion.removeApproved();
        testClassCompanion.removeReceived();
        try {
            approvals.verify("text");
        } catch (AssertionError e) {
            String received = testClassCompanion.readReceived();
            assertThat(received).isEqualTo("text");
        }
        testClassCompanion.removeReceived();
        testClassCompanion.removeApproved();
    }

    @Test
    void approvalShouldKeepReceivedFileWhenApprovedFileMismatch() throws Throwable {
        testClassCompanion.writeApproved("approved");
        try {
            approvals.verify("text");
        } catch (AssertionError e) {
            String received = testClassCompanion.readReceived();
            assertThat(received).isEqualTo("text");
        }
        testClassCompanion.removeReceived();
        testClassCompanion.removeApproved();
    }

    @Test
    void approvalShouldRemoveReceivedFileWhenApprovedFileMatch() throws Throwable {
        testClassCompanion.writeReceived("last content");
        testClassCompanion.writeApproved("same");
        approvals.verify("same");
        String received = testClassCompanion.readReceived();
        assertThat(received).isNull();
        testClassCompanion.removeApproved();
    }

}