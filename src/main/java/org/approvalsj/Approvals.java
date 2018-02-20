package org.approvalsj;

import org.approvalsj.reporter.Reporter;
import org.approvalsj.reporter.ThrowsReporter;
import org.approvalsj.util.TestClassCompanion;

public class Approvals {
    private final TestClassCompanion testClassCompanion;
    private final Reporter[] reporters;

    public Approvals(Class<?> testedClass, Reporter... reporters) {
        testClassCompanion = new TestClassCompanion(testedClass);
        this.reporters = reporters.length == 0
                ? new Reporter[]{new ThrowsReporter()}
                : reporters;
    }

    public void verify(Object actual) throws Throwable {
        if (matchesApprovedFile(actual)) {
            testClassCompanion.removeReceived();
        } else {
            for (Reporter reporter : reporters) {
                reporter.mismatch(testClassCompanion.approvedFile(), testClassCompanion.receivedFile());
            }
        }
    }

    private boolean matchesApprovedFile(Object actual) {
        String approved = testClassCompanion.readApproved();
        testClassCompanion.writeReceived(actual.toString());
        return null != approved && approved.equals(actual.toString());
    }
}
