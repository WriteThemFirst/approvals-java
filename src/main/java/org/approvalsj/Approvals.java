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
        String approved = testClassCompanion.readApproved();
        testClassCompanion.writeReceived(actual.toString());
        if (approved == null) {
            for (Reporter reporter : reporters) {
                reporter.missing(testClassCompanion.approvedFile(), testClassCompanion.receivedFile());
            }
        } else if (!approved.equals(actual.toString())) {
            for (Reporter reporter : reporters) {
                reporter.mismatch(testClassCompanion.approvedFile(), testClassCompanion.receivedFile());
            }
        } else {
            testClassCompanion.removeReceived();
        }
    }
}
