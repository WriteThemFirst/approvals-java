package org.approvalsj;

import org.approvalsj.util.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static java.nio.file.Files.delete;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

        AssertionError error = assertThrows(AssertionError.class, () -> approvals.verify("actual text"));
        assertEquals("expected: <expected text> but was: <actual text>", error.getMessage());

        fileUtils.removeApproved();
    }

//    @Test
//    void approvalShouldFailWhenApprovedFileDoesNotExist() throws Exception {
//        try {
//            delete(fileUtils.approvedFile());
//        } catch (IOException e) {
//            // we were cleaning just in case
//        }
//
//        assertThrows(AssertionError.class, () -> approvals.verify("text"));
//    }


}