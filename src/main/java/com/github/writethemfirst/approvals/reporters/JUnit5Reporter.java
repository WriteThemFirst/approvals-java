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
import com.github.writethemfirst.approvals.reporters.windows.CommandReporter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;

import static com.github.writethemfirst.approvals.utils.FileUtils.silentRead;
import static java.lang.String.format;

/**
 * This `Reporter` uses reflection to throw the same exceptions that the JUnit 5 framework would send. It is useful when
 * no executable can be found by any {@link CommandReporter} but JUnit 5 is on the classpath.
 */
public class JUnit5Reporter implements Reporter {

    private final String JUNIT5_ASSERTIONS = "org.junit.jupiter.api.Assertions";

    /**
     * @throws AssertionError   if the 2 contents do not match
     * @throws RuntimeException if something went wrong with reflection calls to JUnit framework (should not happen if
     *                          you checked {@link #isAvailable()} first)
     */
    @Override
    public void mismatch(Path approved, Path received) {
        try {
            Class<?> testCaseClass = Class.forName(JUNIT5_ASSERTIONS);
            Method assertEquals = testCaseClass.getMethod("assertEquals", Object.class, Object.class, String.class);
            assertEquals.invoke(null,
                silentRead(approved),
                silentRead(received),
                format("%s differs from %s", received, approved));
        } catch (InvocationTargetException e) {
            throw (AssertionError) e.getCause();
        } catch (NoSuchMethodException | IllegalAccessException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isAvailable() {
        try {
            Class.forName(JUNIT5_ASSERTIONS);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

}
