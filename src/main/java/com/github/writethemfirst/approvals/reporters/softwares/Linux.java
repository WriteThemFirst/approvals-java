package com.github.writethemfirst.approvals.reporters.softwares;

import com.github.writethemfirst.approvals.Reporter;
import com.github.writethemfirst.approvals.reporters.ExecReporter;

public interface Linux {

    Reporter IDEA = new ExecReporter("idea merge %approved% %received% %approved%");

}
