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
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

 class ApprovalsSimpleTest {
    private Approvals approvals = new Approvals(new ThrowsReporter());
    private ApprovalsFiles approvalsFiles = new ApprovalsFiles();


    @Test
    void shouldDoNothingWhenApprovedFileExistsAndIsCorrect() {
        approvalsFiles.writeApproved("some text");
        approvals.verify("some text");
        approvalsFiles.removeApproved();
    }


    @Test
    void shouldFailWhenApprovedFileExistsAndIsDifferent() {
        approvalsFiles.writeApproved("expected text");

        assertThatThrownBy(() -> approvals.verify("actual text"))
            .isInstanceOf(AssertionError.class)
            .hasMessage("expected: <expected text> but was: <actual text>");

        approvalsFiles.removeApproved();
        approvalsFiles.removeReceived();
    }


    @Test
    void shouldFailWhenApprovedFileDoesNotExist() {
        approvalsFiles.removeApproved();

        assertThatThrownBy(() -> approvals.verify("text"))
            .isInstanceOf(AssertionError.class)
            .hasMessageContaining("expected: <> but was: <text>");

        approvalsFiles.removeReceived();
    }


    @Test
    void shouldKeepReceivedFileWhenApprovedFileDoesNotExist() {
        approvalsFiles.removeApproved();
        approvalsFiles.removeReceived();
        try {
            approvals.verify("text");
        } catch (AssertionError e) {
            String received = approvalsFiles.readReceived();
            assertThat(received).isEqualTo("text");
        }
        approvalsFiles.removeReceived();
        approvalsFiles.removeApproved();
    }


    @Test
    void shouldKeepReceivedFileWhenApprovedFileMismatch() {
        approvalsFiles.writeApproved("approved");
        try {
            approvals.verify("text");
        } catch (AssertionError e) {
            String received = approvalsFiles.readReceived();
            assertThat(received).isEqualTo("text");
        }
        approvalsFiles.removeReceived();
        approvalsFiles.removeApproved();
    }


    @Test
    void shouldRemoveReceivedFileWhenApprovedFileMatch() {
        approvalsFiles.writeReceived("last content");
        approvalsFiles.writeApproved("same");

        approvals.verify("same");

        String received = approvalsFiles.readReceived();
        assertThat(received).isEqualTo("");

        approvalsFiles.removeApproved();
    }

     @Test
     void shouldCreateEmptyApprovedFile() {
         approvalsFiles.removeReceived();
         approvalsFiles.removeApproved();

         try {
             approvals.verify("new content");
         } catch (AssertionError e){
             //expected
         }

         assertThat(approvalsFiles.approvedFile()).exists();

         approvalsFiles.removeReceived();
         approvalsFiles.removeApproved();
     }

}
