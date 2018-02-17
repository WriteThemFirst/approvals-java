package org.approvalsj;

public class Approvals {
    private final Class<?> testedClass;

    public Approvals(Class<?> testedClass) {
        this.testedClass = testedClass;
    }

    public void verify(Object actual) {
    }
}
