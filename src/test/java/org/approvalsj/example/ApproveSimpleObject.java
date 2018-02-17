package org.approvalsj.example;

import org.approvalsj.Approvals;
import org.approvalsj.util.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;

public class ApproveSimpleObject {
    Approvals approvals = new Approvals(getClass());

    @Test
    void approvalShouldDoNothingWhenApprovedFileExistsAndIsCorrect() throws Exception {
        FileUtils fileUtils = new FileUtils(getClass());
        Path approvedFile = fileUtils.getApprovedFile();

        Files.createDirectories(approvedFile.getParent());
        try (BufferedWriter writer = Files.newBufferedWriter(approvedFile)) {
            writer.write("some test");
        }

        approvals.verify("some text");
        Files.delete(approvedFile);
    }


}