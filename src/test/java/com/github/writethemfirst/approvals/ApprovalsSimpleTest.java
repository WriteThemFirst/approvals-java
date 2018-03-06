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

import com.github.writethemfirst.approvals.files.ApprobationContext;
import com.github.writethemfirst.approvals.files.ApprovalsFiles;
import com.github.writethemfirst.approvals.reporters.CommandReporter;
import com.github.writethemfirst.approvals.reporters.ThrowsReporter;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class ApprovalsSimpleTest {
    private Approvals approvals = new Approvals(new ThrowsReporter());
    private ApprobationContext approbationContext = new ApprobationContext();


    @Test
    void shouldThrowWhenMismatchAndUsingCommandReporter() {
        CommandReporter reporter = mock(CommandReporter.class);
        Approvals approvals = new Approvals(reporter);
        ApprovalsFiles context = approbationContext.defaultFiles();

        context.approvedFile.writeApproved("approved text");

        assertThatThrownBy(() -> approvals.verify("actual text"))
            .isInstanceOf(AssertionError.class)
            .hasMessage("expected: <approved text> but was: <actual text>");

        context.approvedFile.removeApproved();
        context.receivedFile.removeReceived();
    }

    @Test
    void shouldDoNothingWhenApprovedFileExistsAndIsCorrect() {
        ApprovalsFiles context = approbationContext.defaultFiles();
        context.approvedFile.writeApproved("some text");
        approvals.verify("some text");
        context.approvedFile.removeApproved();
    }


    @Test
    void shouldFailWhenApprovedFileExistsAndIsDifferent() {
        ApprovalsFiles context = approbationContext.defaultFiles();

        context.approvedFile.writeApproved("expected text");

        assertThatThrownBy(() -> approvals.verify("actual text"))
            .isInstanceOf(AssertionError.class)
            .hasMessage("expected: <expected text> but was: <actual text>");

        context.approvedFile.removeApproved();
        context.receivedFile.removeReceived();
    }


    @Test
    void shouldFailWhenApprovedFileDoesNotExist() {
        ApprovalsFiles context = approbationContext.defaultFiles();

        context.approvedFile.removeApproved();

        assertThatThrownBy(() -> approvals.verify("text"))
            .isInstanceOf(AssertionError.class)
            .hasMessageContaining("expected: <> but was: <text>");

        context.receivedFile.removeReceived();
        context.approvedFile.removeApproved();
    }


    @Test
    void shouldKeepReceivedFileWhenApprovedFileDoesNotExist() {
        ApprovalsFiles context = approbationContext.defaultFiles();
        context.approvedFile.removeApproved();
        context.receivedFile.removeReceived();
        try {
            approvals.verify("text");
        } catch (AssertionError e) {
            String received = context.receivedFile.readReceived();
            assertThat(received).isEqualTo("text");
        }
        context.receivedFile.removeReceived();
        context.approvedFile.removeApproved();
    }


    @Test
    void shouldKeepReceivedFileWhenApprovedFileMismatch() {
        ApprovalsFiles context = approbationContext.defaultFiles();
        context.approvedFile.writeApproved("approved");
        try {
            approvals.verify("text");
        } catch (AssertionError e) {
            String received = context.receivedFile.readReceived();
            assertThat(received).isEqualTo("text");
        }
        context.receivedFile.removeReceived();
        context.approvedFile.removeApproved();
    }


    @Test
    void shouldRemoveReceivedFileWhenApprovedFileMatch() {
        ApprovalsFiles context = approbationContext.defaultFiles();
        context.receivedFile.writeReceived("last content");
        context.approvedFile.writeApproved("same");

        approvals.verify("same");

        String received = context.receivedFile.readReceived();
        assertThat(received).isEqualTo("");

        context.approvedFile.removeApproved();
    }

    @Test
    void shouldCreateEmptyApprovedFile() {
        ApprovalsFiles context = approbationContext.defaultFiles();
        context.receivedFile.removeReceived();
        context.approvedFile.removeApproved();

        try {
            approvals.verify("new content");
        } catch (AssertionError e) {
            //expected
        }

        assertThat(context.approvedFile.approvedFile()).exists();

        context.receivedFile.removeReceived();
        context.approvedFile.removeApproved();
    }

    @Test
    void shouldUseSpecificMethodName() {
        ApprovalsFiles context = approbationContext.customFiles("myScalaMethod");
        context.receivedFile.removeReceived();
        context.approvedFile.removeApproved();

        try {
            approvals.verify("new content", "myScalaMethod");
        } catch (AssertionError e) {
            //expected
        }

        assertThat(context.receivedFile.receivedFile()).hasContent("new content");

        context.receivedFile.removeReceived();
        context.approvedFile.removeApproved();
    }

}
