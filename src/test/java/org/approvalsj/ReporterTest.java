package org.approvalsj;

import org.approvalsj.reporter.MismatchReporter;
import org.approvalsj.util.FileUtils;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReporterTest {
    private MismatchReporter mismatchReporter = mock(MismatchReporter.class);
    private Approvals approvals = new Approvals(getClass(), mismatchReporter);
    private FileUtils fileUtils = new FileUtils(getClass());

    @Test
    void approvalsShouldCallReporterWhenMismatch() throws Throwable {
        fileUtils.writeApproved("some text");
        try {
            approvals.verify("different text");
        } catch (AssertionError e) {
            verify(mismatchReporter).reportMismatch(fileUtils.approvedFile(), fileUtils.receivedFile());
            fileUtils.removeApproved();
            fileUtils.removeReceived();
        }
    }

   @Test
    void approvalsShouldCallReporterWhenNoApprovedFile() throws Throwable {
        fileUtils.removeApproved();
        try {
            approvals.verify("text");
        } catch (AssertionError e) {
            verify(mismatchReporter).reportMissing(fileUtils.approvedFile(), fileUtils.receivedFile());
            fileUtils.removeApproved();
            fileUtils.removeReceived();
        }
    }

}