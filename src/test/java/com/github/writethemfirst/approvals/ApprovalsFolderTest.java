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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ApprovalsFolderTest {
    private Approvals approvals = new Approvals(new ThrowsReporter());
    private ApprovalsFiles approvalsFiles = new ApprovalsFiles();


    @Test
    void shouldDoNothingWhenBothFoldersAreEmpty() throws IOException {
        //GIVEN
        Files.createDirectories(approvalsFiles.approvedFolder());
        Path parent = Files.createTempDirectory("shouldDoNothingWhenBothFoldersAreEmpty");

        //WHEN
        approvals.verifyAgainstMasterFolder(parent);

        //THEN no exception should be thrown

    }

    @Test
    void shouldThrowWhenAFileIsMissing() throws IOException {
        Path parent = Files.createTempDirectory("shouldThrowWhenAFileIsMissing");

        assertThatThrownBy(() -> approvals.verifyAgainstMasterFolder(parent))
            .isInstanceOf(AssertionError.class)
            .hasMessageContaining("missing file <sample.xml>");

    }

    @Test
    void shouldThrowWhenAFileIsDifferent() throws IOException {
        Path parent = Files.createTempDirectory("shouldThrowWhenAFileIsDifferent");
        Files.createFile(parent.resolve("sample.xml"));
        assertThatThrownBy(() -> approvals.verifyAgainstMasterFolder(parent))
            .isInstanceOf(AssertionError.class)
            .hasMessageContaining("compared to reference <sample.xml.approved>, content differs");

    }
}
