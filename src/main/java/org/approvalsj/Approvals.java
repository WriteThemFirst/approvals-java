package org.approvalsj;

import org.approvalsj.reporter.Reporter;
import org.approvalsj.util.FileUtils;

import static java.lang.String.format;

public class Approvals {
    private final FileUtils fileUtils;
    private final Reporter[] reporters;

    public Approvals(Class<?> testedClass, Reporter... reporters) {
        fileUtils = new FileUtils(testedClass);
        this.reporters = reporters;
    }

    public void verify(Object actual) throws Throwable {
        String approved = fileUtils.readApproved();
        fileUtils.writeReceived(actual.toString());
        if (approved == null) {
            for (Reporter reporter : reporters) {
                reporter.missing(fileUtils.approvedFile(), fileUtils.receivedFile());
            }
            throw new AssertionError(fileUtils.approvedFile() + " does not exist yet");
        } else if (!approved.equals(actual.toString())) {
            String detailMessage = format("expected: <%s> but was: <%s>", approved, actual);
            for (Reporter reporter : reporters) {
                reporter.mismatch(fileUtils.approvedFile(), fileUtils.receivedFile());
            }
            throw new AssertionError(detailMessage);
        } else {
            fileUtils.removeReceived();
        }
    }
}
