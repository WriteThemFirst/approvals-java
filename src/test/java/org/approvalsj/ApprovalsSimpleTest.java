package org.approvalsj;

import org.approvalsj.util.FileUtils;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ApprovalsSimpleTest {
    private Approvals approvals = new Approvals(getClass());
    private FileUtils fileUtils = new FileUtils(getClass());

    @Test
    void approvalShouldDoNothingWhenApprovedFileExistsAndIsCorrect() {
        fileUtils.writeApproved("some text");
        approvals.verify("some text");
        fileUtils.removeApproved();
    }

    @Test
    void approvalShouldFailWhenApprovedFileExistsAndIsDifferent() {
        fileUtils.writeApproved("expected text");

        assertThatThrownBy(() -> approvals.verify("actual text"))
                .isInstanceOf(AssertionError.class)
                .hasMessage("expected: <expected text> but was: <actual text>");

        fileUtils.removeApproved();
    }

    @Test
    void approvalShouldFailWhenApprovedFileDoesNotExist() {
        fileUtils.removeApproved();

        assertThatThrownBy(() -> approvals.verify("text"))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("approvalShouldFailWhenApprovedFileDoesNotExist.approved does not exist yet");

        fileUtils.removeReceived();
    }


    @Test
    void approvalShouldKeepReceivedFileWhenApprovedFileDoesNotExist() {
        fileUtils.removeApproved();
        fileUtils.removeReceived();
        try {
            approvals.verify("text");
        } catch (AssertionError e) {
            String received = fileUtils.readReceived();
            assertThat(received).isEqualTo("text");
        }
        fileUtils.removeReceived();
        fileUtils.removeApproved();
    }

    @Test
    void approvalShouldKeepReceivedFileWhenApprovedFileMismatch() {
        fileUtils.removeApproved();
        fileUtils.writeReceived("different");
        try {
            approvals.verify("text");
        } catch (AssertionError e) {
            String received = fileUtils.readReceived();
            assertThat(received).isEqualTo("text");
        }
        fileUtils.removeReceived();
        fileUtils.removeApproved();
    }

    @Test
    void approvalShouldRemoveReceivedFileWhenApprovedFileMatch() {
        fileUtils.writeReceived("last content");
        fileUtils.writeApproved("same");
        approvals.verify("same");
        String received = fileUtils.readReceived();
        assertThat(received).isNull();
        fileUtils.removeApproved();
    }

}