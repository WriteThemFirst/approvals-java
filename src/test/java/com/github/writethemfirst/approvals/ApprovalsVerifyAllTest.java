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

import java.util.Arrays;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class ApprovalsVerifyAllTest {
    @Test
    void shouldReportMismatchWithSingleArgument() {
        final Reporter reporter = mock(Reporter.class);
        final Approvals approvals = new Approvals(reporter);
        final SimpleTestUtils testUtils = new SimpleTestUtils("shouldReportMismatchWithSingleArgument", getClass());

        try {
            approvals.verifyAll(new Integer[]{1, 2, 3}, x -> x + 1);
        } catch (final AssertionError e) {
            // expected
        }

        assertThat(testUtils.readReceived())
            .contains("(1) => 2")
            .contains("(2) => 3")
            .contains("(3) => 4");

        testUtils.cleanupPaths();
    }

    @Test
    void shouldReportMismatchWithTwoArguments() {
        final Reporter reporter = mock(Reporter.class);
        final Approvals approvals = new Approvals(reporter);
        final SimpleTestUtils testUtils = new SimpleTestUtils("shouldReportMismatchWithTwoArguments", getClass());

        try {
            approvals.verifyAll(
                asList(1, 2),
                asList(4, 6),
                (x, y) -> x + y);
        } catch (final AssertionError e) {
            // expected
        }

        assertThat(testUtils.readReceived())
            .contains("(1,4) => 5")
            .contains("(2,6) => 8");

        testUtils.cleanupPaths();
    }

    @Test
    void shouldReportMismatchWithFiveArguments() {
        final Reporter reporter = mock(Reporter.class);
        final Approvals approvals = new Approvals(reporter);
        final SimpleTestUtils testUtils = new SimpleTestUtils("shouldReportMismatchWithFiveArguments", getClass());

        try {
            approvals.verifyAll(
                asList(1, 2),
                asList(3, 4),
                asList(5, 6),
                asList(7, 8),
                asList(9, 10),
                (x, y, a, b, c) -> x + y + a + b + c);
        } catch (final AssertionError e) {
            // expected
        }

        assertThat(testUtils.readReceived())
            .contains("(1,4,6,7,9) => 27")
            .contains("(2,3,6,8,10) => 29");

        testUtils.cleanupPaths();
    }
}
