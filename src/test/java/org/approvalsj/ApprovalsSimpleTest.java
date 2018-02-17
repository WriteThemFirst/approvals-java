package org.approvalsj;

import org.approvalsj.util.FileUtils;
import org.junit.jupiter.api.Test;

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
    }

}