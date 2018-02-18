package org.demo.simple;

import org.approvalsj.Approvals;
import org.junit.jupiter.api.Test;

import static org.approvalsj.approbation.Approver.GvimApprover;

public class ApproveString {
    Approvals approvals = new Approvals(getClass(), GvimApprover);


    @Test
    void verifySimpleString() {
        approvals.verify("my string");
    }
}
