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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.codehaus.groovy.runtime.StackTraceUtils;

import static java.lang.String.format;
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
 */
public class StackUtils {

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
    public static String callerClass(final Class<?>... potentialReferenceClasses) {
        // FIXME: Rewrite using dropWhile when switching to Java 9
        final List<String> classesInStack = distinctClassesInStack();
        return firstMatchingCallerClass(classesInStack, potentialReferenceClasses);
    }

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
    private static String firstMatchingCallerClass(final List<String> classesInStack, final Class<?>[] potentialReferenceClasses) {
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
    private static List<String> distinctClassesInStack() {
        return stream(santizedStackTrace())
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
    public static Optional<String> callerMethod(final String referenceClassName) {
        return stream(santizedStackTrace())
            .filter(e -> e.getClassName().equals(sanitizeClassName(referenceClassName)))
            .filter(e -> !e.getMethodName().startsWith("lambda$"))
            .map(StackTraceElement::getMethodName)
            .findFirst();
    }

    /**
     * Remove all apparently Groovy-internal trace entries from the stacktrace.
     * 
     * The Groovy stacktrace is usually littered with lots of intermediate calls. Also in some version of Groovy, you
     * find that a (static) method is represented as an inner class which is then called from
     * org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(), resp. callStatic(), which is not handled by
     * Groovy's StackTraceUtils.sanitize().
     * 
     * @return An array of the stacktrace elements without internal Groovy entries.
     */
    private static StackTraceElement[] santizedStackTrace() {
        Throwable t = new Throwable();
        StackTraceUtils.sanitize(t);
        return stream(t.getStackTrace())
            .filter(e -> !isGroovyInnerClassCallFromAbstractCallSite(e))
            .toArray(StackTraceElement[]::new);
    }

    /**
     * Decide whether this is an inner class that actually represents a method being called by AbstractCallSite
     * 
     * @param e the StackTraceElement to be evaluated
     * @return the decision, if this is actually a method represented as an inner class
     */
    private static boolean isGroovyInnerClassCallFromAbstractCallSite(StackTraceElement e) {
        String methodName = e.getMethodName();
        return (methodName.equals("callStatic") || methodName.equals("call") )
            && e.getClassName().matches(".*\\$\\p{javaLowerCase}[^.]*");
    }

    /**
     * Remove the $_methodName_closure1 String part from a Groovy closure class name.
     * 
     * Groovy closures are represented as inner classes. This method extracts the actual class name for these cases.
     * 
     * @param referenceClassName The `referenceClass` for which we want to search the caller method in the current
     *                           thread stacktrace
     * @return The sanitized class name
     */
    public static String sanitizeClassName(String referenceClassName) {
        return referenceClassName.replaceAll("\\$_.*_closure\\d+", "");
    }
}
