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

import com.github.writethemfirst.approvals.reporters.ThrowsReporter;
import com.github.writethemfirst.approvals.reporters.windows.CommandReporter;
import com.github.writethemfirst.approvals.testutils.SimpleTestUtils;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class ApprovalsSimpleTest {
    private final Approvals approvals = new Approvals().reportTo(new ThrowsReporter());


    @Test
    void shouldThrowWhenMismatchAndUsingCommandReporter() {
        final CommandReporter reporter = mock(CommandReporter.class);
        final Approvals approvals = new Approvals().reportTo(reporter);
        final SimpleTestUtils testUtils = new SimpleTestUtils("shouldThrowWhenMismatchAndUsingCommandReporter", getClass());

        testUtils.writeApproved("approved text");

        assertThatThrownBy(() -> approvals.verify("actual text"))
            .isInstanceOf(AssertionError.class)
            .hasMessage("expected: <approved text> but was: <actual text>");

        testUtils.cleanupPaths();
    }

    @Test
    void shouldDoNothingWhenApprovedFileExistsAndIsCorrect() {
        final SimpleTestUtils testUtils = new SimpleTestUtils("shouldDoNothingWhenApprovedFileExistsAndIsCorrect", getClass());
        testUtils.writeApproved("some text");

        approvals.verify("some text");

        testUtils.cleanupPaths();
    }


    @Test
    void shouldFailWhenApprovedFileExistsAndIsDifferent() {
        final SimpleTestUtils testUtils = new SimpleTestUtils("shouldFailWhenApprovedFileExistsAndIsDifferent", getClass());
        testUtils.writeApproved("expected text");

        assertThatThrownBy(() -> approvals.verify("actual text"))
            .isInstanceOf(AssertionError.class)
            .hasMessage("expected: <expected text> but was: <actual text>");

        testUtils.cleanupPaths();
    }


    @Test
    void shouldFailWhenApprovedFileDoesNotExist() {
        final SimpleTestUtils testUtils = new SimpleTestUtils("shouldFailWhenApprovedFileDoesNotExist", getClass());
        testUtils.cleanupPaths();

        assertThatThrownBy(() -> approvals.verify("text"))
            .isInstanceOf(AssertionError.class)
            .hasMessageContaining("expected: <> but was: <text>");

        testUtils.cleanupPaths();
    }


    @Test
    void shouldKeepReceivedFileWhenApprovedFileDoesNotExist() {
        final SimpleTestUtils testUtils = new SimpleTestUtils("shouldKeepReceivedFileWhenApprovedFileDoesNotExist", getClass());
        testUtils.cleanupPaths();

        try {
            approvals.verify("text");
        } catch (final AssertionError e) {
            assertThat(testUtils.readReceived()).isEqualTo("text");
        }

        testUtils.cleanupPaths();
    }


    @Test
    void shouldKeepReceivedFileWhenApprovedFileMismatch() {
        final SimpleTestUtils testUtils = new SimpleTestUtils("shouldKeepReceivedFileWhenApprovedFileMismatch", getClass());
        testUtils.writeApproved("approved");

        try {
            approvals.verify("text");
        } catch (final AssertionError e) {
            assertThat(testUtils.readReceived()).isEqualTo("text");
        }

        testUtils.cleanupPaths();
    }


    @Test
    void shouldRemoveReceivedFileWhenApprovedFileMatch() {
        final SimpleTestUtils testUtils = new SimpleTestUtils("shouldRemoveReceivedFileWhenApprovedFileMatch", getClass());
        testUtils.writeReceived("last content");
        testUtils.writeApproved("same");

        approvals.verify("same");

        assertThat(testUtils.readReceived()).isEqualTo("");

        testUtils.cleanupPaths();
    }

    @Test
    void shouldCreateEmptyApprovedFile() {
        final SimpleTestUtils testUtils = new SimpleTestUtils("shouldCreateEmptyApprovedFile", getClass());
        testUtils.cleanupPaths();

        try {
            approvals.verify("new content");
        } catch (final AssertionError e) {
            //expected
        }

        assertThat(testUtils.approved).exists().hasContent("");

        testUtils.cleanupPaths();
    }

    @Test
    void shouldUseSpecificMethodName() {
        Approvals approvals = new Approvals().writeTo("myScalaMethod").reportTo(new ThrowsReporter());

        final SimpleTestUtils testUtils = new SimpleTestUtils("myScalaMethod", getClass());
        testUtils.cleanupPaths();

        try {
            approvals.verify("new content");
        } catch (final AssertionError e) {
            //expected
        }

        assertThat(testUtils.received).hasContent("new content");

        testUtils.cleanupPaths();
    }
}
