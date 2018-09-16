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
package com.github.writethemfirst.approvals.reporters;

import com.github.writethemfirst.approvals.Reporter;
import com.github.writethemfirst.approvals.files.ApprovedAndReceived;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static java.lang.String.format;

/**
 * # JUnit5Reporter
 *
 * This reporter will only be available if it is able to find JUnit5 classes in the current classpath.
 *
 * If it actually is available, it will allow to throw the same exceptions that JUnit 5 would send while being called
 * for mismatches. This allows to use *Approvals-Java* right from your JUnit 5 Unit Tests, in some `@Test` methods, and
 * fail the tests if mismatches are found.
 *
 * This reporter actually uses reflection to throw the same exceptions JUnit would use.
 */
public class JUnit5Reporter implements Reporter {

    /**
     * The exact class from JUnit 5 we're searching for in the classpath to tell if this reporter is available or not.
     *
     * That is the same class we'll use to actually compute the assertion later on.
     */
    private final String JUNIT5_ASSERTIONS = "org.junit.jupiter.api.Assertions";

    @Override
    public void mismatch(final ApprovedAndReceived files) {
        try {
            final Class<?> assertionsClass = Class.forName(JUNIT5_ASSERTIONS);
            final Method assertEquals = assertionsClass.getMethod("assertEquals",
                Object.class, Object.class, String.class);
            assertEquals.invoke(null, files.approvedContent(), files.receivedContent(),
                format("%s differs from %s", files.received, files.approved));
        } catch (final InvocationTargetException e) {
            //noinspection ThrowInsideCatchBlockWhichIgnoresCaughtException
            throw (AssertionError) e.getCause();
        } catch (NoSuchMethodException | IllegalAccessException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Checks if the {@link #JUNIT5_ASSERTIONS} JUnit 5 class we need is present in the classpath. If so, we assume that
     * JUnit 5 is part of the classpath and that we can use this reporter.
     *
     * @return True if JUnit 5 is considered as being part of the current classpath.
     */
    @Override
    public boolean isAvailable() {
        try {
            Class.forName(JUNIT5_ASSERTIONS);
            return true;
        } catch (final ClassNotFoundException e) {
            return false;
        }
    }
}
