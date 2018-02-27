package com.github.writethemfirst.approvals.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.Thread.currentThread;
import static java.util.Arrays.stream;

/**
 * # StackUtils
 *
 * Set of static methods to be used when parsing of the current Thread stacktrace, allowing to retrieve helpful
 * information about the classes and methods actually calling the *Approval Tests*.
 *
 * Those methods allow to retrieve information to be used for generating the default names of files and folders to be
 * used by the *Approval Files*.
 *
 * @author mdaviot / aneveux
 * @version 1.0
 */
public class StackUtils {

    /**
     * Returns the caller class of `reference` by searching the current thread stacktrace.
     *
     * We consider the caller class to be the first one found in the current thread stacktrace after finding the
     * reference one.
     *
     * @param referenceClass A non-null class for which we want to find the caller class
     * @return The caller class of the provided one (ie. the first one found which isn't the reference after finding the
     * reference)
     */
    public static Class<?> callerClass(final Class<?> referenceClass) {
        //can be rewritten with dropWhile in Java 9
        final List<String> classesInStack = distinctClassesInStack();
        final int referenceIndex = classesInStack.indexOf(referenceClass.getName());
        final String callerClassName = classesInStack.get(referenceIndex + 1);
        try {
            return Class.forName(callerClassName);
        } catch (final ClassNotFoundException e) {
            throw new Error("Unexpected exception", e);
        }
    }

    private static List<String> distinctClassesInStack() {
        return Arrays.stream(currentThread().getStackTrace())
            .map(e -> e.getClassName())
            .distinct()
            .collect(Collectors.toList());
    }

    /**
     * Returns the caller method of the provided `referenceClass`.
     *
     * We consider the caller method to be the first method from the `referenceClass` called in the current thread
     * stacktrace, which isn't a lambda.
     *
     * If found in the current thread stacktrace, the method name will be returned, wrapped in an `Optional`. If no
     * method can be found, an empty `Optional` will be returned.
     *
     * @param referenceClass The `referenceClass` for which we want to search the caller method in the current thread
     *                       stacktrace
     * @return An `Optional` object containing either the caller method name (as a `String`) or an empty value if it
     * cannot be found
     */
    public static Optional<String> callerMethod(final Class<?> referenceClass) {
        String referenceClassName = referenceClass.getName();
        return stream(currentThread().getStackTrace())
            .filter(e -> e.getClassName().equals(referenceClassName))
            .filter(e -> !e.getMethodName().startsWith("lambda$"))
            .map(StackTraceElement::getMethodName)
            .findFirst();
    }
}
