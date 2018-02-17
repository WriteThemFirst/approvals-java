package org.approvalsj;

import org.approvalsj.util.FileUtils;

import static java.lang.String.format;

public class Approvals {
    private final FileUtils fileUtils;

    public Approvals(Class<?> testedClass) {
        fileUtils = new FileUtils(testedClass);
    }

    public void verify(Object actual) {
        String expected = fileUtils.readApproved();
        if (!expected.equals(actual.toString())) {
            String detailMessage = format("expected: <%s> but was: <%s>", expected, actual);
            throw new AssertionError(detailMessage);
        }
    }
}
