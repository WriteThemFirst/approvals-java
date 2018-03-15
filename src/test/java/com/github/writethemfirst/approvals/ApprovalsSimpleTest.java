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

import com.github.writethemfirst.approvals.files.Approbation;
import com.github.writethemfirst.approvals.files.ApprovalsFiles;
import com.github.writethemfirst.approvals.reporters.windows.CommandReporter;
import com.github.writethemfirst.approvals.reporters.ThrowsReporter;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class ApprovalsSimpleTest {
    private Approvals approvals = new Approvals(new ThrowsReporter());
    private Approbation approbation = new Approbation();


    @Test
    void shouldThrowWhenMismatchAndUsingCommandReporter() {
        final CommandReporter reporter = mock(CommandReporter.class);
        final Approvals approvals = new Approvals(reporter);
        final ApprovalsFiles context = approbation.defaultFiles();

        context.approved.write("approved text");

        assertThatThrownBy(() -> approvals.verify("actual text"))
            .isInstanceOf(AssertionError.class)
            .hasMessage("expected: <approved text> but was: <actual text>");

        context.approved.remove();
        context.received.remove();
    }

    @Test
    void shouldDoNothingWhenApprovedFileExistsAndIsCorrect() {
        final ApprovalsFiles context = approbation.defaultFiles();
        context.approved.write("some text");
        approvals.verify("some text");
        context.approved.remove();
    }


    @Test
    void shouldFailWhenApprovedFileExistsAndIsDifferent() {
        final ApprovalsFiles context = approbation.defaultFiles();

        context.approved.write("expected text");

        assertThatThrownBy(() -> approvals.verify("actual text"))
            .isInstanceOf(AssertionError.class)
            .hasMessage("expected: <expected text> but was: <actual text>");

        context.approved.remove();
        context.received.remove();
    }


    @Test
    void shouldFailWhenApprovedFileDoesNotExist() {
        final ApprovalsFiles context = approbation.defaultFiles();

        context.approved.remove();

        assertThatThrownBy(() -> approvals.verify("text"))
            .isInstanceOf(AssertionError.class)
            .hasMessageContaining("expected: <> but was: <text>");

        context.received.remove();
        context.approved.remove();
    }


    @Test
    void shouldKeepReceivedFileWhenApprovedFileDoesNotExist() {
        final ApprovalsFiles context = approbation.defaultFiles();
        context.approved.remove();
        context.received.remove();
        try {
            approvals.verify("text");
        } catch (final AssertionError e) {
            final String received = context.received.read();
            assertThat(received).isEqualTo("text");
        }
        context.received.remove();
        context.approved.remove();
    }


    @Test
    void shouldKeepReceivedFileWhenApprovedFileMismatch() {
        final ApprovalsFiles context = approbation.defaultFiles();
        context.approved.write("approved");
        try {
            approvals.verify("text");
        } catch (final AssertionError e) {
            final String received = context.received.read();
            assertThat(received).isEqualTo("text");
        }
        context.received.remove();
        context.approved.remove();
    }


    @Test
    void shouldRemoveReceivedFileWhenApprovedFileMatch() {
        final ApprovalsFiles context = approbation.defaultFiles();
        context.received.write("last content");
        context.approved.write("same");

        approvals.verify("same");

        final String received = context.received.read();
        assertThat(received).isEqualTo("");

        context.approved.remove();
    }

    @Test
    void shouldCreateEmptyApprovedFile() {
        final ApprovalsFiles context = approbation.defaultFiles();
        context.received.remove();
        context.approved.remove();

        try {
            approvals.verify("new content");
        } catch (final AssertionError e) {
            //expected
        }

        assertThat(context.approved.get()).exists();

        context.received.remove();
        context.approved.remove();
    }

    @Test
    void shouldUseSpecificMethodName() {
        final ApprovalsFiles context = approbation.customFiles("myScalaMethod");
        context.received.remove();
        context.approved.remove();

        try {
            approvals.verify("new content", "myScalaMethod");
        } catch (final AssertionError e) {
            //expected
        }

        assertThat(context.received.get()).hasContent("new content");

        context.received.remove();
        context.approved.remove();
    }

}
