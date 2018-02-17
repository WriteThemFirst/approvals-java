package org.approvalsj;

import org.approvalsj.util.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.lang.String.format;

public class Approvals {
    private final Class<?> testedClass;

    public Approvals(Class<?> testedClass) {
        this.testedClass = testedClass;
    }

    public void verify(Object actual) {
        Path approvedFile = new FileUtils(testedClass).approvedFile();
        try {
            String expected = Files.newBufferedReader(approvedFile).readLine();
            if (!expected.equals(actual.toString())) {
                String detailMessage = format("expected: <%s> but was: <%s>", expected, actual);
                throw new AssertionError(detailMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
