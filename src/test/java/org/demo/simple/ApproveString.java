package org.demo.simple;

import static com.github.writethemfirst.approvals.reporters.softwares.Windows.IDEA;

import com.github.writethemfirst.approvals.Approvals;
import com.github.writethemfirst.approvals.reporters.JUnit5Reporter;
import org.junit.jupiter.api.Test;


class ApproveString {
    private Approvals approvals = new Approvals(IDEA, new JUnit5Reporter());


    @Test
    void verifySimpleString()
        throws Throwable {
        approvals.verify("my string");
    }
}
