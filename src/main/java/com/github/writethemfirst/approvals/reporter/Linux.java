package com.github.writethemfirst.approvals.reporter;

public interface Linux {

    Reporter IDEA = new ExecReporter("idea merge %approved% %received% %approved%");

}
