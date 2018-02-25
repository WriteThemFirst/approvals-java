package com.github.writethemfirst.approvals.reporters.softwares;

import com.github.writethemfirst.approvals.Reporter;
import com.github.writethemfirst.approvals.reporters.FirstWorkingReporter;
import com.github.writethemfirst.approvals.reporters.JUnit5Reporter;
import com.github.writethemfirst.approvals.reporters.ThrowsReporter;

public interface Generic {

    String os = System.getProperty("os.name");
    boolean isWindows = os.startsWith("Windows");

    Reporter DEFAULT = isWindows
        ? Windows.DEFAULT
        : new FirstWorkingReporter(new JUnit5Reporter(), new ThrowsReporter());
}
