package org.approvalsj;

import org.approvalsj.util.FileUtils;

import static java.lang.String.format;

public class Approvals {
    private final FileUtils fileUtils;

    public Approvals(Class<?> testedClass) {
        fileUtils = new FileUtils(testedClass);
    }

    public void verify(Object actual) {
        String approved = fileUtils.readApproved();
        fileUtils.writeReceived(actual.toString());
        if (approved == null) {
            throw new AssertionError(fileUtils.approvedFile() + " does not exist yet");
        } else if (!approved.equals(actual.toString())) {
            String detailMessage = format("expected: <%s> but was: <%s>", approved, actual);
            throw new AssertionError(detailMessage);
        } else {
            fileUtils.removeReceived();
        }
    }
}
