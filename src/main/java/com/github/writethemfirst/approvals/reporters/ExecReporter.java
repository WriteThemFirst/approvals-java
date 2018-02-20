package com.github.writethemfirst.approvals.reporters;

import com.github.writethemfirst.approvals.Reporter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;

import static java.lang.Runtime.getRuntime;
import static java.lang.String.format;
import static java.util.Arrays.stream;

public class ExecReporter implements Reporter {
    private final String[] command;
    private final String programFiles = System.getenv("ProgramFiles");

    public ExecReporter(String command) {
        this.command = command.split(" ");
    }

    public ExecReporter(String... command) {
        this.command = command;
    }

    @Override
    public void mismatch(Path approved, Path received) {
        String[] cmdLine = stream(command).map(elt -> elt
                .replace("%programFiles%", programFiles == null ? "" : programFiles)
                .replace("%approved%", approved.toString())
                .replace("%received%", received.toString()))
                .toArray(String[]::new);
        try {
            getRuntime().exec(cmdLine);
        } catch (IOException e) {
            System.err.println(format("Cannot execute %s : %s", Arrays.toString(cmdLine), e));
        }
    }
}
