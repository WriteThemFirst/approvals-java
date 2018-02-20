package org.approvalsj;

import org.approvalsj.reporter.Reporter;
import org.approvalsj.util.TestClassCompanion;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReporterTest {
    private Reporter reporter = mock(Reporter.class);
    private Approvals approvals = new Approvals(getClass(), reporter);
    private TestClassCompanion testClassCompanion = new TestClassCompanion(getClass());

    @Test
    void approvalsShouldCallReporterWhenMismatch() throws Throwable {
        testClassCompanion.writeApproved("some text");

        approvals.verify("different text");

        verify(reporter).mismatch(testClassCompanion.approvedFile(), testClassCompanion.receivedFile());

        testClassCompanion.removeApproved();
        testClassCompanion.removeReceived();
    }

    @Test
    void approvalsShouldCallReporterWhenNoApprovedFile() throws Throwable {
        testClassCompanion.removeApproved();

        approvals.verify("text");

        verify(reporter).missing(testClassCompanion.approvedFile(), testClassCompanion.receivedFile());

        testClassCompanion.removeApproved();
        testClassCompanion.removeReceived();
    }

}