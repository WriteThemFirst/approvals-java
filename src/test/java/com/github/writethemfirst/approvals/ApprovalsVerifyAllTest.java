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

import com.github.writethemfirst.approvals.approvers.Approver;
import com.github.writethemfirst.approvals.testutils.SimpleTestUtils;
import org.junit.jupiter.api.Test;

import static com.github.writethemfirst.approvals.utils.FunctionUtils.applyCombinations;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class ApprovalsVerifyAllTest {
    private final Reporter reporter = mock(Reporter.class);
    private final Approver approver = new Approver().reportTo(reporter);

    @Test
    void shouldReportMismatchWithSingleArgument() {
        final SimpleTestUtils testUtils = new SimpleTestUtils("shouldReportMismatchWithSingleArgument", getClass());

        try {
            final String combinations = applyCombinations(asList(1, 2, 3), x -> x + 1);
            approver.verify(combinations);
        } catch (final AssertionError e) {
            // expected
        }

        assertThat(testUtils.readReceived())
            .contains("2 <== , 1")
            .contains("3 <== , 2")
            .contains("4 <== , 3");

        testUtils.cleanupPaths();
    }

    @Test
    void shouldReportMismatchWithTwoArguments() {
        final SimpleTestUtils testUtils = new SimpleTestUtils("shouldReportMismatchWithTwoArguments", getClass());
        try {
            final String combinations = applyCombinations(
                asList(1, 2),
                asList(4, 6),
                (x, y) -> x + y);
            approver.verify(combinations);
        } catch (final AssertionError e) {
            // expected
        }

        assertThat(testUtils.readReceived())
            .contains("5 <== , 1, 4")
            .contains("8 <== , 2, 6");

        testUtils.cleanupPaths();
    }

    @Test
    void shouldReportMismatchWithFiveArguments() {

        final SimpleTestUtils testUtils =
            new SimpleTestUtils("shouldReportMismatchWithFiveArguments", getClass());

        try {
            final String combinations = applyCombinations(
                asList(1, 2),
                asList(3, 4),
                asList(5, 6),
                asList(7, 8),
                asList(9, 10),
                (x, y, a, b, c) -> x + y + a + b + c);
            approver.namedArguments("x", "y", "a", "b", "c").verify(combinations);
        } catch (final AssertionError e) {
            // expected
        }

        assertThat(testUtils.readReceived())
            .contains("result, x, y, a, b, c")
            .contains("27 <== , 1, 4, 6, 7, 9")
            .contains("29 <== , 2, 3, 6, 8, 10");

        testUtils.cleanupPaths();
    }
}
