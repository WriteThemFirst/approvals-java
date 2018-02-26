package com.github.writethemfirst.approvals;

import org.junit.jupiter.api.Test;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;


public class ReporterTest {
    private Reporter reporter = mock(Reporter.class);
    private Approvals approvals = new Approvals(reporter);
    private ApprovalsFiles approvalsFiles = new ApprovalsFiles(getClass());


    @Test
    void approvalsShouldCallReporterWhenMismatch() {
        approvalsFiles.writeApproved("some text");

        approvals.verify("different text");

        then(reporter).should().mismatch(approvalsFiles.approvedFile(), approvalsFiles.receivedFile());

        approvalsFiles.removeApproved();
        approvalsFiles.removeReceived();
    }


    @Test
    void approvalsShouldCallReporterWhenNoApprovedFile() {
        approvalsFiles.removeApproved();

        approvals.verify("text");

        then(reporter).should().mismatch(approvalsFiles.approvedFile(), approvalsFiles.receivedFile());

        approvalsFiles.removeApproved();
        approvalsFiles.removeReceived();
    }

}
