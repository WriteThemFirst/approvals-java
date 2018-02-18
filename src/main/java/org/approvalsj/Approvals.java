package org.approvalsj;

import org.approvalsj.reporter.MismatchReporter;
import org.approvalsj.util.FileUtils;

import java.util.Optional;

import static java.lang.String.format;

public class Approvals {
    private final FileUtils fileUtils;
    private final Optional<MismatchReporter> approver;

    public Approvals(Class<?> testedClass) {
        this(testedClass, Optional.empty());
    }

    public Approvals(Class<?> testedClass, MismatchReporter mismatchReporter) {
        this(testedClass, Optional.of(mismatchReporter));
    }

    private Approvals(Class<?> testedClass, Optional<MismatchReporter> approver) {
        fileUtils = new FileUtils(testedClass);
        this.approver = approver;
    }

    public void verify(Object actual) {
        String approved = fileUtils.readApproved();
        fileUtils.writeReceived(actual.toString());
        if (approved == null) {
            throw new AssertionError(fileUtils.approvedFile() + " does not exist yet");
        } else if (!approved.equals(actual.toString())) {
            String detailMessage = format("expected: <%s> but was: <%s>", approved, actual);
            approver.ifPresent(a -> a.reportMismatch(fileUtils.approvedFile(), fileUtils.receivedFile()));
            throw new AssertionError(detailMessage);
        } else {
            fileUtils.removeReceived();
        }
    }
}
