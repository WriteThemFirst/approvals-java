package org.approvalsj;

import org.approvalsj.util.FileUtils;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Approvals {
    private final Class<?> testedClass;

    public Approvals(Class<?> testedClass) {
        this.testedClass = testedClass;
    }

    public void verify(Object actual) {
        Path approvedFile = new FileUtils(testedClass).getApprovedFile();
        String expected = null;
        try {
            expected = Files.newBufferedReader(approvedFile).readLine();
            Assertions.assertEquals(expected, actual.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
