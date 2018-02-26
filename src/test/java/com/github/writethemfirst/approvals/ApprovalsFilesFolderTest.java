package com.github.writethemfirst.approvals;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.github.writethemfirst.approvals.utils.FileUtils.silentRemoveRec;
import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.createFile;
import static org.assertj.core.api.Assertions.assertThat;

class ApprovalsFilesFolderTest {
    private ApprovalsFiles companion = new ApprovalsFiles();


    @Test
    void approvedFolderShouldBeExpectedPath() {
        //WHEN
        Path approvedFile = companion.approvedFolder();

        //THEN
        Path expectedPath = Paths.get("src/test/resources/com/github/writethemfirst/approvals" +
            "/ApprovalsFilesFolderTest/approvedFolderShouldBeExpectedPath.Files");
        assertThat(approvedFile).isEqualTo(expectedPath);
    }

    @Test
    void approvedFolderShouldContainListedFiles() throws IOException {
        //GIVEN
        Path parent = Paths.get("src/test/resources/com/github/writethemfirst/approvals" +
            "/ApprovalsFilesFolderTest/approvedFolderShouldContainListedFiles.Files");
        silentRemoveRec(parent.toFile());
        createDirectories(parent.resolve("sub"));
        Path xml = parent.resolve("sample.xml.expected");
        Path csv = parent.resolve("sub/ref.csv.expected");
        createFile(xml);
        createFile(csv);

        //WHEN
        List<Path> paths = companion.approvedFilesInFolder();

        //THEN
        assertThat(paths).containsExactlyInAnyOrder(xml, csv);

        silentRemoveRec(parent.toFile());
    }

}
