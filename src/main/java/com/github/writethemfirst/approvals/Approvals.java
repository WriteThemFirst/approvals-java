package com.github.writethemfirst.approvals;

import com.github.writethemfirst.approvals.reporters.ThrowsReporter;

public class Approvals {
    private final ApprovalsFiles approvalsFiles;
    private final Reporter[] reporters;


    public Approvals(Class<?> testedClass, Reporter... reporters) {
        approvalsFiles = new ApprovalsFiles(testedClass);
        this.reporters = reporters.length == 0
                ? new Reporter[]{new ThrowsReporter()}
                : reporters;
    }


    public void verify(Object actual)
            throws Throwable {
        if(matchesApprovedFile(actual)) {
            approvalsFiles.removeReceived();
        } else {
            for(Reporter reporter : reporters) {
                reporter.mismatch(approvalsFiles.approvedFile(), approvalsFiles.receivedFile());
            }
        }
    }


    private boolean matchesApprovedFile(Object actual) {
        String approved = approvalsFiles.readApproved();
        approvalsFiles.writeReceived(actual.toString());
        return null != approved && approved.equals(actual.toString());
    }
}
