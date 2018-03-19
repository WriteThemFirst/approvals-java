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
package com.github.writethemfirst.approvals;

import com.github.writethemfirst.approvals.testutils.SimpleTestUtils;
import org.junit.jupiter.api.Test;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;


class ReporterTest {
    private final Reporter reporter = mock(Reporter.class);
    private final Approvals approvals = new Approvals().reportTo(reporter);


    @Test
    void approvalsShouldCallReporterWhenMismatch() {
        final SimpleTestUtils testUtils = new SimpleTestUtils("approvalsShouldCallReporterWhenMismatch", getClass(), "");
        testUtils.writeApproved("some text");

        try {
            approvals.verify("different text");
        } catch (final AssertionError e) {
            // expected
        }

        then(reporter).should().mismatch(testUtils.approved, testUtils.received);

        testUtils.cleanupPaths();
    }


    @Test
    void approvalsShouldCallReporterWhenNoApprovedFile() {
        final SimpleTestUtils testUtils = new SimpleTestUtils("approvalsShouldCallReporterWhenNoApprovedFile", getClass(), "");
        testUtils.cleanupPaths();

        try {
            approvals.verify("text");
        } catch (final AssertionError e) {
            // expected
        }

        then(reporter).should().mismatch(testUtils.approved, testUtils.received);

        testUtils.cleanupPaths();
    }
}
