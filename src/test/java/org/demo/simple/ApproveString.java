package org.demo.simple;

import org.approvalsj.Approvals;
import org.approvalsj.approbation.ExecApprover;
import org.junit.jupiter.api.Test;

public class ApproveString {
    Approvals approvals = new Approvals(
            getClass(),
            new ExecApprover("cmd /c gvimdiff \"%s\" \"%s\""));


    @Test
    void verifySimpleString() {
        approvals.verify("my string");
    }
}
