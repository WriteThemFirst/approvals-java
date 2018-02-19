package org.approvalsj;

import org.approvalsj.reporter.Reporter;
import org.approvalsj.util.FileUtils;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReporterTest {
    private Reporter reporter = mock(Reporter.class);
    private Approvals approvals = new Approvals(getClass(), reporter);
    private FileUtils fileUtils = new FileUtils(getClass());

    @Test
    void approvalsShouldCallReporterWhenMismatch() throws Throwable {
        fileUtils.writeApproved("some text");

        approvals.verify("different text");

        verify(reporter).mismatch(fileUtils.approvedFile(), fileUtils.receivedFile());

        fileUtils.removeApproved();
        fileUtils.removeReceived();
    }

    @Test
    void approvalsShouldCallReporterWhenNoApprovedFile() throws Throwable {
        fileUtils.removeApproved();

        approvals.verify("text");

        verify(reporter).missing(fileUtils.approvedFile(), fileUtils.receivedFile());

        fileUtils.removeApproved();
        fileUtils.removeReceived();
    }

}