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
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FirstWorkingReporterTest {
    @Test
    void shouldUseFirstWorkingReporter() {
        final Reporter reporter1 = mock(Reporter.class);
        final Reporter reporter2 = mock(Reporter.class);
        final Reporter reporter3 = mock(Reporter.class);
        final Path a = Paths.get("a");
        final Path b = Paths.get("b");
        when(reporter1.isAvailable()).thenReturn(false);
        when(reporter2.isAvailable()).thenReturn(true);
        when(reporter3.isAvailable()).thenReturn(true);
        final ApprovalFiles approvalFiles = new ApprovalFiles(a, b);

        final FirstWorkingReporter firstWorkingReporter = new FirstWorkingReporter(reporter1, reporter2, reporter3);


        firstWorkingReporter.mismatch(approvalFiles);

        then(reporter2).should().mismatch(approvalFiles);

    }

    @Test
    void shouldIgnoreWhenNoReporterIsAvailable() {
        final Reporter reporter1 = mock(Reporter.class);
        final Path a = Paths.get("a");
        final Path b = Paths.get("b");
        when(reporter1.isAvailable()).thenReturn(false);
        final ApprovalFiles approvalFiles = new ApprovalFiles(a, b);

        final FirstWorkingReporter firstWorkingReporter = new FirstWorkingReporter(reporter1);

        firstWorkingReporter.mismatch(approvalFiles); // should not throw
    }

}
