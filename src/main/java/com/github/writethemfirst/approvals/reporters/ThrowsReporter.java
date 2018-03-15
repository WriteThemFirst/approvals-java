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

import java.nio.file.Path;

import static com.github.writethemfirst.approvals.utils.FileUtils.silentRead;
import static java.lang.String.format;

/**
 * The most basic `Reporter`, it simply throws an {@link AssertionError} in case of mismatch between *approved* and
 * *received*. It is always available.
 */
public class ThrowsReporter implements Reporter {
    /**
     * @throws AssertionError if the 2 contents do not match
     */
    @Override
    public void mismatch(final Path approvedPath, final Path receivedPath) {
        final String approved = silentRead(approvedPath);
        final String actual = silentRead(receivedPath);
        final String detailMessage = format("expected: <%s> but was: <%s>", approved, actual);
        throw new AssertionError(detailMessage);
    }

    @Override
    public boolean isAvailable() {
        return true;
    }
}
