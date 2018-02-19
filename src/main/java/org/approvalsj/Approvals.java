package org.approvalsj;

import org.approvalsj.reporter.Reporter;
import org.approvalsj.reporter.ThrowsReporter;
import org.approvalsj.util.FileUtils;

public class Approvals {
    private final FileUtils fileUtils;
    private final Reporter[] reporters;

    public Approvals(Class<?> testedClass, Reporter... reporters) {
        fileUtils = new FileUtils(testedClass);
        this.reporters = reporters.length == 0
                ? new Reporter[]{new ThrowsReporter()}
                : reporters;
    }

    public void verify(Object actual) throws Throwable {
        String approved = fileUtils.readApproved();
        fileUtils.writeReceived(actual.toString());
        if (approved == null) {
            for (Reporter reporter : reporters) {
                reporter.missing(fileUtils.approvedFile(), fileUtils.receivedFile());
            }
        } else if (!approved.equals(actual.toString())) {
            for (Reporter reporter : reporters) {
                reporter.mismatch(fileUtils.approvedFile(), fileUtils.receivedFile());
            }
        } else {
            fileUtils.removeReceived();
        }
    }
}
