package org.demo.simple;

import static com.github.writethemfirst.approvals.reporter.Windows.IDEA;

import com.github.writethemfirst.approvals.Approvals;
import com.github.writethemfirst.approvals.reporter.JUnit5Reporter;
import org.junit.jupiter.api.Test;


class ApproveString {
    private Approvals approvals = new Approvals(getClass(), IDEA, new JUnit5Reporter());


    @Test
    void verifySimpleString()
            throws Throwable {
        approvals.verify("my string");
    }
}
