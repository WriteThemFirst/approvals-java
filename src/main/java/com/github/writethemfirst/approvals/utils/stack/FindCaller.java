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
package com.github.writethemfirst.approvals.utils.stack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.Thread.currentThread;
import static java.util.Arrays.stream;

/**
 * # FindCaller
 *
 * Used when parsing of the current Thread stacktrace, allowing to retrieve helpful information about the classes and
 * methods actually calling the *Approval Tests*.
 *
 * Those methods allow to retrieve information to be used for generating the default names of files and folders to be
 * used by the *Approval Files*.
 *
 * This interface can have implementations specific to a JVM language or a test framework.
 */
public interface FindCaller {


    /**
     * Returns the caller class (from the classes in the current stack trace) of the first matching reference class
     * (from the potential reference classes provided).
     *
     * @param classesInStack            A list of all distinct classes' names from the current thread stacktrace
     * @param potentialReferenceClasses An array of all potential reference classes to use to search for a caller class.
     *                                  The first class which is found in the current stack trace will be used as
     *                                  reference
     * @return The name of the caller class of the first matching reference class
     */
    static String firstMatchingCallerClass(final List<String> classesInStack, final Class<?>[] potentialReferenceClasses) {
        final ArrayList<String> reversedStack = new ArrayList<>(classesInStack);
        Collections.reverse(reversedStack);
        final Optional<String> lastReferenceName = reversedStack.stream()
            .filter(className -> stream(potentialReferenceClasses).anyMatch(
                referenceClass -> className.equals(referenceClass.getName())))
            .findFirst();

        if (lastReferenceName.isPresent()) {
            final int referenceIndex = classesInStack.indexOf(lastReferenceName.get());
            if (referenceIndex + 1 < classesInStack.size()) {
                return classesInStack.get(referenceIndex + 1);
            } else {
                System.err.println("Reference class is found but appears to have no parent in the current stack trace...");
            }
        } else {
            System.err.println("Can't locate any of the provided reference classes in the current stack trace...");
        }
        return "";
    }

    /**
     * Parses the current thread stacktrace and returns a list of all distinct class names found in it.
     *
     * @return A List containing all distinct classes' names from the current thread stacktrace
     */
    static List<String> distinctClassesInStack() {
        return stream(currentThread().getStackTrace())
            .map(StackTraceElement::getClassName)
            .distinct()
            .collect(Collectors.toList());
    }

    /**
     * Returns the caller class of the first potential reference class found by searching the current thread
     * stacktrace.
     *
     * We consider the caller class to be the first one found in the current thread stacktrace after finding the first
     * potential reference class.
     *
     * @param potentialReferenceClasses An array of all potential reference classes to use to search for a caller class.
     *                                  The first class which is found in the current stack trace will be used as
     *                                  reference
     * @return The caller class name of the first potential reference class found in the current stack trace
     */
    default String callerClass(final Class<?>... potentialReferenceClasses) {
        // FIXME: Rewrite using dropWhile when switching to Java 9
        final List<String> classesInStack = distinctClassesInStack();
        return firstMatchingCallerClass(classesInStack, potentialReferenceClasses);
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
     * @param referenceClassName the class for which we want to search the caller method in the current thread
     *                           stacktrace
     * @return An `Optional` object containing either the caller method name (as a `String`) or an empty value if it
     * cannot be found
     */
    default Optional<String> callerMethod(final String referenceClassName) {
        return stream(currentThread().getStackTrace())
            .filter(e -> e.getClassName().equals(referenceClassName))
            .filter(e -> !e.getMethodName().startsWith("lambda$"))
            .map(StackTraceElement::getMethodName)
            .findFirst();
    }
}
