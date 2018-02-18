package org.demo.simple;

import org.approvalsj.Approvals;
import org.approvalsj.reporter.JUnit4Reporter;
import org.junit.jupiter.api.Test;

public class ApproveString {
    Approvals approvals = new Approvals(getClass(), new JUnit4Reporter());


    @Test
    void verifySimpleString() throws Throwable {
        approvals.verify("my string");
    }
}
