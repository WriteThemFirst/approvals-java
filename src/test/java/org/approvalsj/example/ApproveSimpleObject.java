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
    void approvalShouldDoNothingWhenApprovedFileExists() throws Exception {
        FileUtils fileUtils = new FileUtils(getClass());
        String methodName = "approvalShouldDoNothingWhenApprovedFileExists";
        Path approvedFile = fileUtils.getApprovedFile(methodName);

        Files.createDirectories(approvedFile.getParent());
        try (BufferedWriter writer = Files.newBufferedWriter(approvedFile)) {
            writer.write("some test");
        }

        approvals.verify(methodName,"some text");
        Files.delete(approvedFile);
    }


}