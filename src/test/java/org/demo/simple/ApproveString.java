package org.demo.simple;

import org.approvalsj.Approvals;
import org.junit.jupiter.api.Test;

public class ApproveString {
    Approvals approvals = new Approvals(getClass());

    @Test
    void verifySimpleString() {
        approvals.verify("my string");
    }
}
