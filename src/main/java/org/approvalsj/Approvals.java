package org.approvalsj;

import org.approvalsj.approbation.Approver;
import org.approvalsj.util.FileUtils;

import java.util.Optional;

import static java.lang.String.format;

public class Approvals {
    private final FileUtils fileUtils;
    private final Optional<Approver> approver;

    public Approvals(Class<?> testedClass) {
        this(testedClass, Optional.empty());
    }

    public Approvals(Class<?> testedClass, Approver approver) {
        this(testedClass, Optional.of(approver));
    }

    private Approvals(Class<?> testedClass, Optional<Approver> approver) {
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
            approver.ifPresent(a -> a.approve(fileUtils.approvedFile(), fileUtils.receivedFile()));
            throw new AssertionError(detailMessage);
        } else {
            fileUtils.removeReceived();
        }
    }
}
