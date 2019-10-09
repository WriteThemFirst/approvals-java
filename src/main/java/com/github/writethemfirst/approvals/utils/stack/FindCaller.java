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

import java.util.Optional;

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
 *
 * The implementation must be declared using SPI in the library jar in file
 * META-INF/services/com.github.writethemfirst.approvals.utils.stack.FindCaller
 */
public interface FindCaller {

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
    String callerClass(final Class<?>... potentialReferenceClasses);

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
    Optional<String> callerMethod(final String referenceClassName);
}
