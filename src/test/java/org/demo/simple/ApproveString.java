package org.demo.simple;

import org.approvalsj.Approvals;
import org.approvalsj.reporter.JUnit5Reporter;
import org.junit.jupiter.api.Test;

import static org.approvalsj.reporter.Windows.KDIFF;

public class ApproveString {
    Approvals approvals = new Approvals(getClass(), KDIFF, new JUnit5Reporter());

    @Test
    void verifySimpleString() throws Throwable {
        approvals.verify("my string");
    }
}
