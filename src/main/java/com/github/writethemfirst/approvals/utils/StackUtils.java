package com.github.writethemfirst.approvals.utils;

import java.util.Optional;

import static java.lang.Thread.currentThread;
import static java.util.Arrays.stream;

/**
 * # StackUtils
 *
 * Set of methods to be used for easing the parsing of the current Thread stacktrace, allowing to retrieve helpful
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
     * Returns the caller class of the one provided as an argument by searching for it in the current thread
     * stacktrace.
     *
     * We consider the caller class to be the really first one found in the current thread stacktrace after finding the
     * reference one (and which obviously isn't the reference one).
     *
     * If the caller class can't be found, a `RuntimeException` will be thrown since it isn't supposed to happen.
     *
     * @param referenceClass A non-null class for which we want to find the caller class
     * @return The caller class of the provided one (ie. the first one found which isn't the reference after finding the
     * reference)
     * @throws RuntimeException In case the caller class cannot be found in the current thread stacktrace
     */
    public static Class<?> callerClass(final Class<?> referenceClass) {
        try {
            //can be rewritten with dropWhile in Java 9
            final String reference = referenceClass.getName();
            boolean inApprovals = false;
            for (final StackTraceElement element : currentThread().getStackTrace()) {
                final String elementClass = element.getClassName();
                if (inApprovals && !elementClass.equals(reference)) {
                    return Class.forName(elementClass);
                }
                if (elementClass.equals(reference)) {
                    inApprovals = true;
                }
            }
            throw new RuntimeException("Error: cannot find the caller class of: " + reference);
        } catch (final ClassNotFoundException e) {
            throw new RuntimeException("Error: cannot find the caller class of: " + referenceClass, e);
        }
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
     * @return An `Optional` object contaning either the caller method name (as a `String`) or an empty value if it
     * cannot be found
     */
    public static Optional<String> callerMethod(final Class<?> referenceClass) {
        return stream(currentThread().getStackTrace())
            .filter(e -> e.getClassName().equals(referenceClass.getName()))
            .filter(e -> !e.getMethodName().startsWith("lambda$"))
            .map(StackTraceElement::getMethodName)
            .findFirst();
    }
}
