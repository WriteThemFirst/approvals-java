package org.approvalsj.util;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

class FileUtilsTest {
    private FileUtils fileUtils = new FileUtils(getClass());

    @Test
    void approvedFileShouldBeExpectedPath() {
        //WHEN
        Path approvedFile = fileUtils.approvedFile("approvedFileShouldBeCorrect");

        //THEN
        Path expectedPath = Paths.get("src/test/resources/org/approvalsj/util/FileUtilsTest/approvedFileShouldBeCorrect.approved");
        assertThat(approvedFile).isEqualTo(expectedPath);
    }

    @Test
    void approvedFileShouldBeReadAfterWritten() {
        //WHEN
        String content = "some content\non 2 lines";
        fileUtils.writeApproved(content);

        //THEN
        String actualContent = fileUtils.readApproved();
        assertThat(actualContent).isEqualTo(content);

        //CLEANUP
        fileUtils.removeApproved();
    }

    @Test
    void readApprovedShouldBeNullWhenFileMissing() {
        //GIVEN
        fileUtils.removeApproved();

        //WHEN
        String read = fileUtils.readApproved();

        //THEN
        assertThat(read).isNull();
    }
}