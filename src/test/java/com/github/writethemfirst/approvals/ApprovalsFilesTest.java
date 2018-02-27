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
        Path approvedFile = companion.approvedFile("approvedFileShouldBeCorrect");

        //THEN
        Path expectedPath = Paths.get(
            "src/test/resources/com/github/writethemfirst/approvals/ApprovalsFilesTest/approvedFileShouldBeCorrect.approved");
        assertThat(approvedFile).isEqualTo(expectedPath);
    }


    @Test
    void approvedFileShouldBeReadAfterWritten() {
        //WHEN
        String content = "some content\non 2 lines";
        companion.writeApproved(content);

        //THEN
        String actualContent = companion.readApproved();
        assertThat(actualContent).isEqualTo(content);

        //CLEANUP
        companion.removeApproved();
    }


    @Test
    void receivedFileShouldBeReadAfterWritten() {
        //WHEN
        String content = "some content\non 2 lines";
        companion.writeReceived(content);

        //THEN
        String actualContent = companion.readReceived();
        assertThat(actualContent).isEqualTo(content);

        //CLEANUP
        companion.removeReceived();
    }


    @Test
    void readApprovedShouldBeEmptyWhenFileMissing() {
        //GIVEN
        companion.removeApproved();

        //WHEN
        String read = companion.readApproved();

        //THEN
        assertThat(read).isEqualTo("");
    }

}
