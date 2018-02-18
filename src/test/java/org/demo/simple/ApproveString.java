package org.demo.simple;

import org.approvalsj.Approvals;
import org.junit.jupiter.api.Test;

public class ApproveString {

    @Test
    void verifySimpleString() {
        Approvals approvals = new Approvals(getClass());
        approvals.verify("my string");
    }
}
