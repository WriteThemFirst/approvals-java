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
import com.github.writethemfirst.approvals.files.ApprovalFiles;

import java.util.Optional;
import java.util.stream.Stream;

import static java.lang.String.format;

/**
 * # FirstWorkingReporter
 *
 * Particular Reporter which allows to search in a list of potentially usable reporters for the first one available, and
 * uses it.
 *
 * This Reporter only searches in the reporters which are given to it while constructing it (it won't parse the
 * classpath or anything like this).
 *
 * It also won't try the reporters, but simply rely on their {@link #isAvailable()} methods.
 */
public class FirstWorkingReporter implements Reporter {

    /**
     * Potentially usable reporters given while constructing the {@link FirstWorkingReporter}
     */
    private final Reporter[] reporters;

    /**
     * Potential first working reporter.
     *
     * It is potential cause there might be none of them available in the current execution context.
     */
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private Optional<Reporter> firstWorking;

    /**
     * Constructs a {@link FirstWorkingReporter} with a list of potentially usable reporters
     *
     * Careful: the provided reporters will be parsed in the order you provided them! Be sure to order them in the order
     * you actually want!
     *
     * @param reporters The reporters that could be usable, in which will search for the actual first availabe
     */
    public FirstWorkingReporter(final Reporter... reporters) {
        this.reporters = reporters;
    }

    /**
     * Reports a mismatch between the provided *approved* and *received* files using the first working reporter found.
     *
     * Since the {@link FirstWorkingReporter} is usually used as a fallback reporter, in case no first working reporter
     * can be found and a mismatch is raised, a log entry will be written in System.err.
     * @param files
     */
     @Override
     public void mismatch(final ApprovalFiles files) {
        if (firstWorking().isPresent()) {
            firstWorking().get().mismatch(files);
        } else {
            System.err.println(format("No working reporter for reporting mismatch between %s and %s", files.approved, files.received));
        }
    }

    /**
     * Checks if a working reporter has been found or not.
     *
     * @return true if an available reporter has been found in the provided ones.
     */
    @Override
    public boolean isAvailable() {
        return firstWorking().isPresent();
    }

    /**
     * Returns the first working reporter found in the provided ones. It'll only search for the first working reporter
     * once, and will store it then.
     *
     * @return A potential first working reporter (it'll either return the first one it founds, or empty in case none is
     * found).s
     */
    private Optional<Reporter> firstWorking() {
        final boolean firstTime = firstWorking == null;
        if (firstTime) {
            firstWorking = findFirstAvailable();
        }
        return firstWorking;
    }

    /**
     * Searches through the provided reporters for the first one which is available and returns it wrapped in an Option.
     * If it can't find any working reporter, it'll return `empty`.
     *
     * @return The first available reporter if it exists, or empty otherwise.
     */
    private Optional<Reporter> findFirstAvailable() {
        return Stream.of(reporters).filter(Reporter::isAvailable).findFirst();
    }
}
