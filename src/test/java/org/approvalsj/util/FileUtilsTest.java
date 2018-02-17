package org.approvalsj.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

class FileUtilsTest {

    @Test
    void approvedFileShouldBeCorrect() {
        FileUtils fileUtils = new FileUtils(getClass());

        Path approvedFile = fileUtils.approvedFile("approvedFileShouldBeCorrect");

        Path expectedPath= Paths.get("src/test/resources/org/approvalsj/util/FileUtilsTest/approvedFileShouldBeCorrect.approved");
        Assertions.assertEquals(expectedPath, approvedFile);
    }
}