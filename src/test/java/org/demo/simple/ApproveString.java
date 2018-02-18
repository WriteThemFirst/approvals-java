package org.demo.simple;

import org.approvalsj.Approvals;
import org.junit.jupiter.api.Test;

import static org.approvalsj.approbation.Approver.Kdiff3Approver;

public class ApproveString {
    Approvals approvals = new Approvals(getClass(), Kdiff3Approver);


    @Test
    void verifySimpleString() {
        approvals.verify("my string");
    }
}
