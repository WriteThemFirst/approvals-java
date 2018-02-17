package org.approvalsj.util;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileUtilsTest {

    @Test
    void approvedFileShouldBeCorrect() {
        //GIVEN
        FileUtils fileUtils = new FileUtils(getClass());

        //WHEN
        Path approvedFile = fileUtils.approvedFile("approvedFileShouldBeCorrect");

        //THEN
        Path expectedPath = Paths.get("src/test/resources/org/approvalsj/util/FileUtilsTest/approvedFileShouldBeCorrect.approved");
        assertEquals(expectedPath, approvedFile);
    }

    @Test
    void approvedFileShouldBeReadAfterWritten() throws Exception {
        //GIVEN
        FileUtils fileUtils = new FileUtils(getClass());
        String content = "some content\non 2 lines";

        //WHEN
        fileUtils.writeApproved(content);

        //THEN
        String actualContent = fileUtils.readApproved();
        assertEquals(content, actualContent);

        //CLEANUP
        Files.delete(fileUtils.approvedFile());
    }
}