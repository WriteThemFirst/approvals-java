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

package com.github.writethemfirst.approvals.files;

import com.github.writethemfirst.approvals.Reporter;
import com.github.writethemfirst.approvals.reporters.ThrowsReporter;

import java.util.List;

import static com.github.writethemfirst.approvals.utils.FileUtils.silentRecursiveRemove;
import static com.github.writethemfirst.approvals.utils.FileUtils.silentRemove;

/**
 * Holds 2 lists of matches (files with same content) and mismatches (different files).
 */
public class MatchesAndMismatches {
    private final List<ApprovalFiles> matches;
    private final List<ApprovalFiles> mismatches;

    MatchesAndMismatches(final List<ApprovalFiles> matches, final List<ApprovalFiles> mismatches) {
        this.matches = matches;
        this.mismatches = mismatches;
    }

    public void reportMismatches(final Reporter reporter) {
        if (mismatches.size() > 0) {
            final ApprovalFiles firstMismatch = mismatches.get(0);
            reporter.mismatch(firstMismatch.parent());
        }
    }

    public void throwMismatches() {
        mismatches.forEach(mismatch -> new ThrowsReporter().mismatch(mismatch));
    }

    public void cleanupReceivedFiles() {
        matches.forEach(ar -> silentRemove(ar.received));
        if (mismatches.isEmpty() && !matches.isEmpty()) {
            final ApprovalFiles firstMatch = matches.get(0);
            silentRecursiveRemove(firstMatch.parent().received);
        }
    }
}
