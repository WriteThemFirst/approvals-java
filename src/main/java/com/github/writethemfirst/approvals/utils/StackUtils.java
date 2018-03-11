/*
 * Approvals-Java - Approval testing library for Java. Alleviates the burden of hand-writing assertions.
 * Copyright © 2018 Write Them First!
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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

    /**
     * Parses the current thread stacktrace and returns a list of all distinct class names found in it.
     *
     * @return A List containing all distinct classes' names from the current thread stacktrace
     */
    private static List<String> distinctClassesInStack() {
        return Arrays.stream(currentThread().getStackTrace())
            .map(StackTraceElement::getClassName)
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
        final String referenceClassName = referenceClass.getName();
        return stream(currentThread().getStackTrace())
            .filter(e -> e.getClassName().equals(referenceClassName))
            .filter(e -> !e.getMethodName().startsWith("lambda$"))
            .map(StackTraceElement::getMethodName)
            .findFirst();
    }
}
