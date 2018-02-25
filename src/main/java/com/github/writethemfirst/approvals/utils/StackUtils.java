package com.github.writethemfirst.approvals.utils;

import java.util.Optional;

import static java.lang.Thread.currentThread;
import static java.util.Arrays.stream;

public class StackUtils {
    /**
     * Finds in the stack the caller class which is right before the reference class.
     *
     * @param reference a non-null class which is used for comparison
     * @return the first class in the stack which differs
     */
    public static Class<?> callerClass(Class<?> reference) {
        try {
            //can be rewritten with dropWhile in Java 9
            String refClass = reference.getName();
            boolean inApprovals = false;
            for (StackTraceElement element : currentThread().getStackTrace()) {
                String elementClass = element.getClassName();
                if (inApprovals && !elementClass.equals(refClass)) {
                    return Class.forName(elementClass);
                }

                if (elementClass.equals(refClass)) {
                    inApprovals = true;
                }
            }
            throw new RuntimeException("bug : no class found");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("bug", e);
        }
    }

    /**
     * Gets from the stack the name of the method which was called from the class.
     *
     * @return a method name, or empty if none was found
     */
    public static Optional<String> callerMethod(Class<?> testClass) {
        return stream(currentThread()
            .getStackTrace())
            .filter(e -> e.getClassName()
                .equals(testClass.getName()))
            .filter(e -> !e.getMethodName()
                .startsWith("lambda$"))
            .map(e -> e.getMethodName())
            .findFirst();
    }
}
