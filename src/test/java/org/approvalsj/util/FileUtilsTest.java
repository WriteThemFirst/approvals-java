package org.approvalsj.util;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.Files.delete;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class FileUtilsTest {
    FileUtils fileUtils = new FileUtils(getClass());

    @Test
    void approvedFileShouldBeExpectedPath() {
        //WHEN
        Path approvedFile = fileUtils.approvedFile("approvedFileShouldBeCorrect");

        //THEN
        Path expectedPath = Paths.get("src/test/resources/org/approvalsj/util/FileUtilsTest/approvedFileShouldBeCorrect.approved");
        assertEquals(expectedPath, approvedFile);
    }

    @Test
    void approvedFileShouldBeReadAfterWritten() throws Exception {
        //WHEN
        String content = "some content\non 2 lines";
        fileUtils.writeApproved(content);

        //THEN
        String actualContent = fileUtils.readApproved();
        assertEquals(content, actualContent);

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
        assertNull(read);
    }
}