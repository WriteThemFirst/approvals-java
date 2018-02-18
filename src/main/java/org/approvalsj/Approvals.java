package org.approvalsj;

import org.approvalsj.reporter.MismatchReporter;
import org.approvalsj.util.FileUtils;

import java.util.Optional;

import static java.lang.String.format;

public class Approvals {
    private final FileUtils fileUtils;
    private final MismatchReporter[] mismatchReporters;

    public Approvals(Class<?> testedClass, MismatchReporter... mismatchReporters) {
        fileUtils = new FileUtils(testedClass);
        this.mismatchReporters = mismatchReporters;
    }

    public void verify(Object actual) throws Throwable {
        String approved = fileUtils.readApproved();
        fileUtils.writeReceived(actual.toString());
        if (approved == null) {
            throw new AssertionError(fileUtils.approvedFile() + " does not exist yet");
        } else if (!approved.equals(actual.toString())) {
            String detailMessage = format("expected: <%s> but was: <%s>", approved, actual);
            for (MismatchReporter reporter : mismatchReporters) {
                reporter.reportMismatch(fileUtils.approvedFile(), fileUtils.receivedFile());
            }
            throw new AssertionError(detailMessage);
        } else {
            fileUtils.removeReceived();
        }
    }
}
