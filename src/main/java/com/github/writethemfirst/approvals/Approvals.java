package com.github.writethemfirst.approvals;

import com.github.writethemfirst.approvals.reporters.ThrowsReporter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Approvals {

    private final ApprovalsFiles approvalsFiles;
    private final List<Reporter> reporters;


    public Approvals(final Class<?> clazz, final Reporter... reporters) {
        approvalsFiles = new ApprovalsFiles(clazz);
        this.reporters = reporters.length == 0
            ? Collections.singletonList(new ThrowsReporter())
            : Arrays.asList(reporters);
    }


    public void verify(final Object actual) throws Throwable {
        if (matchesApprovedFile(actual)) {
            approvalsFiles.removeReceived();
        } else {
            for (final Reporter reporter : reporters) {
                reporter.mismatch(approvalsFiles.approvedFile(), approvalsFiles.receivedFile());
            }
        }
    }


    private boolean matchesApprovedFile(final Object actual) {
        final String approvedContent = approvalsFiles.readApproved();
        approvalsFiles.writeReceived(actual.toString());
        return approvedContent != null && approvedContent.equals(actual.toString());
    }
}
