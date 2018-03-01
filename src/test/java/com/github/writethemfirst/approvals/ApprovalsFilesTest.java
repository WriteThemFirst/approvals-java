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

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class ApprovalsFilesTest {
    private ApprovalsFiles companion = new ApprovalsFiles();


    @Test
    void approvedFileShouldBeExpectedPath() {
        //WHEN
        Path approvedFile = companion.context("approvedFileShouldBeCorrect").approvedFile();

        //THEN
        Path expectedPath = Paths.get(
            "src/test/resources/com/github/writethemfirst/approvals/ApprovalsFilesTest/approvedFileShouldBeCorrect.approved");
        assertThat(approvedFile).isEqualTo(expectedPath);
    }


    @Test
    void approvedFileShouldBeReadAfterWritten() {
        ApprobationContext context = companion.defaultContext();
        //WHEN
        String content = "some content\non 2 lines";
        context.writeApproved(content);

        //THEN
        String actualContent = context.readApproved();
        assertThat(actualContent).isEqualTo(content);

        //CLEANUP
        context.removeApproved();
    }

    @Test
    void approvedFileInFolderShouldBeReadAfterWritten() {
        ApprobationContext context = companion.defaultContext();
        //WHEN
        Path file = Paths.get("subfolder/file.txt");
        String content = "some content\non 2 lines";
        context.writeApproved(content, file);

        //THEN
        String actualContent = context.readApproved(file);
        assertThat(actualContent).isEqualTo(content);

        //CLEANUP
        context.removeApproved(file);
    }


    @Test
    void receivedFileShouldBeReadAfterWritten() {
        ApprobationContext context = companion.defaultContext();

        //WHEN
        String content = "some content\non 2 lines";
        context.writeReceived(content);

        //THEN
        String actualContent = context.readReceived();
        assertThat(actualContent).isEqualTo(content);

        //CLEANUP
        context.removeReceived();
    }


    @Test
    void readApprovedShouldBeEmptyWhenFileMissing() {
        ApprobationContext context = companion.defaultContext();

        //GIVEN
        context.removeApproved();

        //WHEN
        String read = context.readApproved();

        //THEN
        assertThat(read).isEqualTo("");
    }

}
