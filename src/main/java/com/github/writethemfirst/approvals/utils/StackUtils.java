package com.github.writethemfirst.approvals.utils;

import static java.lang.Thread.currentThread;

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

}
