package com.github.writethemfirst.approvals;

import com.github.writethemfirst.approvals.reporters.ThrowsReporter;
import org.junit.jupiter.api.Test;

public class ApprovalsFolderTest {
    private Approvals approvals = new Approvals(new ThrowsReporter());
    private ApprovalsFiles approvalsFiles = new ApprovalsFiles(getClass());


    @Test
    void shouldDoNothingWhenApprovedFolderExistsAndIsCorrect() {
        approvalsFiles.writeApproved("some text");
        approvals.verify("some text");
        approvalsFiles.removeApproved();
    }


}
