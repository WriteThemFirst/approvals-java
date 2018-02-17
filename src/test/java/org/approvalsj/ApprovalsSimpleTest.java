package org.approvalsj;

import org.approvalsj.util.FileUtils;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ApprovalsSimpleTest {
    Approvals approvals = new Approvals(getClass());
    FileUtils fileUtils = new FileUtils(getClass());

    @Test
    void approvalShouldDoNothingWhenApprovedFileExistsAndIsCorrect() throws Exception {
        fileUtils.writeApproved("some text");
        approvals.verify("some text");
        fileUtils.removeApproved();
    }

    @Test
    void approvalShouldFailWhenApprovedFileExistsAndIsDifferent() throws Exception {
        fileUtils.writeApproved("expected text");

        assertThatThrownBy(() -> approvals.verify("actual text"))
                .isInstanceOf(AssertionError.class)
                .hasMessage("expected: <expected text> but was: <actual text>");

        fileUtils.removeApproved();
    }

    @Test
    void approvalShouldFailWhenApprovedFileDoesNotExist() throws Exception {
        fileUtils.removeApproved();

        assertThatThrownBy(() -> approvals.verify("text"))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("approvalShouldFailWhenApprovedFileDoesNotExist.approved does not exist yet");
    }

}