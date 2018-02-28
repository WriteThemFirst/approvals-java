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

import com.github.writethemfirst.approvals.reporters.CommandReporter;
import com.github.writethemfirst.approvals.reporters.ThrowsReporter;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class ApprovalsSimpleTest {
    private Approvals approvals = new Approvals(new ThrowsReporter());
    private ApprovalsFiles approvalsFiles = new ApprovalsFiles();


    @Test
    void shouldThrowWhenMismatchAndUsingCommandReporter() {
        CommandReporter reporter = mock(CommandReporter.class);
        Approvals approvals = new Approvals(reporter);
        ApprobationContext context = approvalsFiles.defaultContext();

        context.writeApproved("approved text");

        assertThatThrownBy(() -> approvals.verify("actual text"))
            .isInstanceOf(AssertionError.class)
            .hasMessage("expected: <approved text> but was: <actual text>");

        context.removeApproved();
        context.removeReceived();
    }

    @Test
    void shouldDoNothingWhenApprovedFileExistsAndIsCorrect() {
        ApprobationContext context = approvalsFiles.defaultContext();
        context.writeApproved("some text");
        approvals.verify("some text");
        context.removeApproved();
    }


    @Test
    void shouldFailWhenApprovedFileExistsAndIsDifferent() {
        ApprobationContext context = approvalsFiles.defaultContext();

        context.writeApproved("expected text");

        assertThatThrownBy(() -> approvals.verify("actual text"))
            .isInstanceOf(AssertionError.class)
            .hasMessage("expected: <expected text> but was: <actual text>");

        context.removeApproved();
        context.removeReceived();
    }


    @Test
    void shouldFailWhenApprovedFileDoesNotExist() {
        ApprobationContext context = approvalsFiles.defaultContext();

        context.removeApproved();

        assertThatThrownBy(() -> approvals.verify("text"))
            .isInstanceOf(AssertionError.class)
            .hasMessageContaining("expected: <> but was: <text>");

        context.removeReceived();
        context.removeApproved();
    }


    @Test
    void shouldKeepReceivedFileWhenApprovedFileDoesNotExist() {
        ApprobationContext context = approvalsFiles.defaultContext();
        context.removeApproved();
        context.removeReceived();
        try {
            approvals.verify("text");
        } catch (AssertionError e) {
            String received = context.readReceived();
            assertThat(received).isEqualTo("text");
        }
        context.removeReceived();
        context.removeApproved();
    }


    @Test
    void shouldKeepReceivedFileWhenApprovedFileMismatch() {
        ApprobationContext context = approvalsFiles.defaultContext();
        context.writeApproved("approved");
        try {
            approvals.verify("text");
        } catch (AssertionError e) {
            String received = context.readReceived();
            assertThat(received).isEqualTo("text");
        }
        context.removeReceived();
        context.removeApproved();
    }


    @Test
    void shouldRemoveReceivedFileWhenApprovedFileMatch() {
        ApprobationContext context = approvalsFiles.defaultContext();
        context.writeReceived("last content");
        context.writeApproved("same");

        approvals.verify("same");

        String received = context.readReceived();
        assertThat(received).isEqualTo("");

        context.removeApproved();
    }

    @Test
    void shouldCreateEmptyApprovedFile() {
        ApprobationContext context = approvalsFiles.defaultContext();
        context.removeReceived();
        context.removeApproved();

        try {
            approvals.verify("new content");
        } catch (AssertionError e) {
            //expected
        }

        assertThat(context.approvedFile()).exists();

        context.removeReceived();
        context.removeApproved();
    }

//    @Test
//    void shouldUseSpecificFileName() {
//        silentRemove(approvalsFiles.approvedFile("myScalaMethod"));
//        silentRemove(approvalsFiles.receivedFile("myScalaMethod"));
//
//        try {
//            approvals.verify("new content", "myScalaMethod");
//        } catch (AssertionError e) {
//            //expected
//        }
//
//        assertThat(approvalsFiles.receivedFile("myScalaMethod")).hasContent("new content");
//
//        approvalsFiles.removeReceived();
//        approvalsFiles.removeApproved();
//    }

}
