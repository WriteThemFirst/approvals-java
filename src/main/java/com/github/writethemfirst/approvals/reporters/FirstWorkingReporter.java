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
import java.util.Optional;

/**
 * `Reporter` implementation which delegates to the first of its known reporter
 * for which {@link #isAvailable()} is true.
 */
public class FirstWorkingReporter implements Reporter {
    private final Reporter[] reporters;
    private Optional<Reporter> firstAvailable;

    public FirstWorkingReporter(Reporter... reporters) {
        this.reporters = reporters;
    }

    @Override
    public void mismatch(Path approved, Path received) {
        if (firstAvailable().isPresent()) {
            firstAvailable().get().mismatch(approved, received);
        }
    }

    @Override
    public boolean isAvailable() {
        return firstAvailable().isPresent();
    }

    private Optional<Reporter> firstAvailable() {
        boolean firstTime = firstAvailable == null;
        if (firstTime) {
            firstAvailable = findFirstAvailable();
        }
        return firstAvailable;
    }

    private Optional<Reporter> findFirstAvailable() {
        for (Reporter reporter : reporters) {
            if (reporter.isAvailable()) {
                return Optional.of(reporter);
            }
        }
        return Optional.empty();
    }
}
