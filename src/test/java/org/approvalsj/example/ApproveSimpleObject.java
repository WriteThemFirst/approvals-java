package org.approvalsj.example;

import org.approvalsj.Approvals;
import org.approvalsj.util.FileUtils;
import org.junit.jupiter.api.Test;

import static java.nio.file.Files.delete;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ApproveSimpleObject {
    Approvals approvals = new Approvals(getClass());
    FileUtils fileUtils = new FileUtils(getClass());

    @Test
    void approvalShouldDoNothingWhenApprovedFileExistsAndIsCorrect() throws Exception {
        fileUtils.writeApproved("some text");
        approvals.verify("some text");
        delete(fileUtils.approvedFile());
    }

    @Test
    void approvalShouldFailWhenApprovedFileExistsAndIsDifferent() throws Exception {
        fileUtils.writeApproved("expected text");

        AssertionError error = assertThrows(AssertionError.class, () -> approvals.verify("actual text"));
        assertEquals("expected: <expected text> but was: <actual text>", error.getMessage());

        delete(fileUtils.approvedFile());
    }


}