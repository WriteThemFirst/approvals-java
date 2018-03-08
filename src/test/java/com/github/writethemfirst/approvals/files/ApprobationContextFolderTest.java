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

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.github.writethemfirst.approvals.utils.FileUtils.silentRecursiveRemove;
import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.createFile;
import static org.assertj.core.api.Assertions.assertThat;

class ApprobationContextFolderTest {
    private ApprobationContext companion = new ApprobationContext();


    @Test
    void approvedFolderShouldBeExpectedPath() {
        //WHEN
        Path approvedFile = companion.defaultFiles().approvalsFolder();

        //THEN
        Path expectedPath = Paths.get("src/test/resources/com/github/writethemfirst/approvals/files" +
            "/ApprobationContextFolderTest/approvedFolderShouldBeExpectedPath.Files");
        assertThat(approvedFile).isEqualTo(expectedPath);
    }

    @Test
    void approvedFolderShouldContainListedFiles() throws IOException {
        //GIVEN
        Path parent = Paths.get("src/test/resources/com/github/writethemfirst/approvals/files" +
            "/ApprobationContextFolderTest/approvedFolderShouldContainListedFiles.Files");
        silentRecursiveRemove(parent.toFile());
        createDirectories(parent.resolve("sub"));
        Path xml = parent.resolve("sample.xml.approved");
        Path csv = parent.resolve("sub/ref.csv.approved");
        createFile(xml);
        createFile(csv);

        //WHEN
        List<Path> paths = companion.defaultFiles().approvedFilesInFolder();

        //THEN
        assertThat(paths).containsExactlyInAnyOrder(xml, csv);

        silentRecursiveRemove(parent.toFile());
    }

}
