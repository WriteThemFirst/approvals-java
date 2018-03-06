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

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

class ApprobationContextTest {
    private ApprobationContext companion = new ApprobationContext();


    @Test
    void approvedFileShouldBeExpectedPath() {
        //WHEN
        Path approvedFile = companion.customFiles("approvedFileShouldBeCorrect").approvedFile.approvedFile();

        //THEN
        Path expectedPath = Paths.get(
            "src/test/resources/com/github/writethemfirst/approvals/files/ApprobationContextTest/approvedFileShouldBeCorrect.approved");
        assertThat(approvedFile).isEqualTo(expectedPath);
    }


    @Test
    void approvedFileShouldBeReadAfterWritten() {
        ApprovalsFiles context = companion.defaultFiles();
        //WHEN
        String content = "some content\non 2 lines";
        context.approvedFile.writeApproved(content);

        //THEN
        String actualContent = context.approvedFile.readApproved();
        assertThat(actualContent).isEqualTo(content);

        //CLEANUP
        context.approvedFile.removeApproved();
    }

    @Test
    void approvedFileInFolderShouldBeReadAfterWritten() {
        ApprovalsFiles context = companion.defaultFiles();
        //WHEN
        Path file = Paths.get("subfolder/file.txt");
        String content = "some content\non 2 lines";
        context.approvedFile.writeApproved(content, file);

        //THEN
        String actualContent = context.approvedFile.readApproved(file);
        assertThat(actualContent).isEqualTo(content);

        //CLEANUP
        context.approvedFile.removeApproved(file);
    }


    @Test
    void receivedFileShouldBeReadAfterWritten() {
        ApprovalsFiles context = companion.defaultFiles();

        //WHEN
        String content = "some content\non 2 lines";
        context.receivedFile.writeReceived(content);

        //THEN
        String actualContent = context.receivedFile.readReceived();
        assertThat(actualContent).isEqualTo(content);

        //CLEANUP
        context.receivedFile.removeReceived();
    }


    @Test
    void readApprovedShouldBeEmptyWhenFileMissing() {
        ApprovalsFiles context = companion.defaultFiles();

        //GIVEN
        context.approvedFile.removeApproved();

        //WHEN
        String read = context.approvedFile.readApproved();

        //THEN
        assertThat(read).isEqualTo("");
    }

}
