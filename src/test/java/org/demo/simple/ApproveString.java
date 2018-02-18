package org.demo.simple;

import org.approvalsj.Approvals;
import org.junit.jupiter.api.Test;

import static org.approvalsj.reporter.MismatchReporter.KDIFF;

public class ApproveString {
    Approvals approvals = new Approvals(getClass(), KDIFF);


    @Test
    void verifySimpleString() {
        approvals.verify("my string");
    }
}
