package org.approvalsj.util;

import java.util.Arrays;
import java.util.Optional;

public class StackUtils {
    private final String className;

    public StackUtils(Class<?> aClass) {
        className = aClass.getName();
    }

    /**
     * Gets from the stack the name of the method which was called from the class.
     *
     * @return a method name, or empty if none was found
     */
    public Optional<String> methodName() {
        return Arrays.stream(Thread.currentThread().getStackTrace())
                .filter(e -> e.getClassName().equals(className))
                .map(e -> e.getMethodName())
                .findFirst();
    }
}
