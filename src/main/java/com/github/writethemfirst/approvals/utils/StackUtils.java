package com.github.writethemfirst.approvals.utils;

import static java.lang.Thread.currentThread;

public class StackUtils {
    public static Class<?> guessClass(Class<?> reference) {
        try {
            //can be rewritten with dropWhile in Java 9
            String ourClass = reference.getName();
            String className = null;
            boolean inApprovals = false;
            for (StackTraceElement element : currentThread().getStackTrace()) {
                String elementClass = element.getClassName();
                if (inApprovals && !elementClass.equals(ourClass)) {
                    className = elementClass;
                    break;
                }
                if (elementClass.equals(ourClass)) {
                    inApprovals = true;
                }
            }
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("bug", e);
        }
    }
}
