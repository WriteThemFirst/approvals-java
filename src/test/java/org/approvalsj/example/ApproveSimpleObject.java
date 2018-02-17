package org.approvalsj.example;

import org.approvalsj.Approvals;
import org.approvalsj.util.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ApproveSimpleObject {
    Approvals approvals = new Approvals(getClass());

    @Test
    void approvalShouldDoNothingWhenApprovedFileExistsAndIsCorrect() throws Exception {
        FileUtils fileUtils = new FileUtils(getClass());
        Path approvedFile = fileUtils.getApprovedFile();

        Files.createDirectories(approvedFile.getParent());
        try (BufferedWriter writer = Files.newBufferedWriter(approvedFile)) {
            writer.write("some text");
        }

        approvals.verify("some text");
        Files.delete(approvedFile);
    }

    @Test
    void approvalShouldFailWhenApprovedFileExistsAndIsDifferent() throws Exception {
        FileUtils fileUtils = new FileUtils(getClass());
        Path approvedFile = fileUtils.getApprovedFile();

        Files.createDirectories(approvedFile.getParent());
        try (BufferedWriter writer = Files.newBufferedWriter(approvedFile)) {
            writer.write("expected test");
        }

        AssertionError error = assertThrows(AssertionError.class, () -> approvals.verify("actual text"));
        assertEquals("expected: <expected test> but was: <actual text>", error.getMessage());

        Files.delete(approvedFile);
    }


}