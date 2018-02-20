package org.approvalsj.reporter;

import java.io.IOException;
import java.nio.file.Path;

import static java.lang.Runtime.*;
import static java.lang.String.*;

public class ExecReporter implements Reporter {
    private final String command;
    final String programFiles = System.getenv("ProgramFiles");

    public ExecReporter(String command) {
        this.command = command;
    }

    @Override
    public void missing(Path approved, Path received) {
        mismatch(approved, received);
    }

    @Override
    public void mismatch(Path approved, Path received) {
        String cmdLine = command
                .replace("%programFiles%", programFiles == null ? "" : programFiles.toString())
                .replace("%approved%", approved.toString())
                .replace("%received%", received.toString());
        try {
            getRuntime().exec(cmdLine);
        } catch (IOException e) {
            System.err.println(format("Cannot execute %s : %s", cmdLine, e));
        }
    }
}
