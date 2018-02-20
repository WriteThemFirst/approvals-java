package com.github.writethemfirst.approvals;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

public class ReporterTest {
    private Reporter reporter = mock(Reporter.class);
    private Approvals approvals = new Approvals(getClass(), reporter);
    private ApprovalsFiles approvalsFiles = new ApprovalsFiles(getClass());


    @Test
    void approvalsShouldCallReporterWhenMismatch()
            throws Throwable {
        approvalsFiles.writeApproved("some text");

        approvals.verify("different text");

        verify(reporter).mismatch(approvalsFiles.approvedFile(), approvalsFiles.receivedFile());

        approvalsFiles.removeApproved();
        approvalsFiles.removeReceived();
    }


    @Test
    void approvalsShouldCallReporterWhenNoApprovedFile()
            throws Throwable {
        approvalsFiles.removeApproved();

        approvals.verify("text");

        verify(reporter).mismatch(approvalsFiles.approvedFile(), approvalsFiles.receivedFile());

        approvalsFiles.removeApproved();
        approvalsFiles.removeReceived();
    }

}
