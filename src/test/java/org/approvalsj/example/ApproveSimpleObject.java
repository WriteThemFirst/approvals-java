package org.approvalsj.example;

import org.approvalsj.Approvals;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ApproveSimpleObject {
    Approvals approvals = new Approvals();

    @Test
    void approvalShouldDoNothingWhenApprovedFileExists() throws Exception {
        String packageName = getClass().getPackage().getName();
        String path = "src/test/resources/" + Arrays.stream(packageName.split("\\.")).collect(Collectors.joining("/"));
        String fileName = "ApproveSimpleObject-approvalShouldDoNothingWhenApprovedFileExists.approved";

        Files.createDirectories(Paths.get(path));
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(path, fileName))) {
            writer.write("some test");
        }


        approvals.verify("some text");
    }

}