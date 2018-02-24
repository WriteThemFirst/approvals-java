package com.github.writethemfirst.approvals.reporters;

import com.github.writethemfirst.approvals.Reporter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;

import static java.lang.Runtime.getRuntime;
import static java.lang.String.format;
import static java.util.Arrays.stream;

/**
 * A Reporter implementation which runs a command external to the JVM.
 *
 * A subclass exists for Windows to handle "Program Files".
 */
public class ExecReporter implements Reporter {
    private final String[] command;

    public ExecReporter(String command) {
        this.command = command.split(" ");
    }

    public ExecReporter(String... command) {
        this.command = command;
    }

    @Override
    public void mismatch(Path approved, Path received) {
        String[] cmdLine = getCmdLine(approved, received);
        try {
            getRuntime().exec(cmdLine);
        } catch (IOException e) {
            System.err.println(format("Cannot execute %s : %s", Arrays.toString(cmdLine), e));
        }
    }

    /**
     * Prepares the command line by substituting %approved% and %received% tags with actual files.
     */
    String[] getCmdLine(Path approved, Path received) {
        return stream(command)
            .map(elt -> prepareCommandElement(approved, received, elt))
            .toArray(String[]::new);
    }

    private String prepareCommandElement(Path approved, Path received, String elt) {
        return osReplace(elt)
            .replace("%approved%", approved.toString())
            .replace("%received%", received.toString());
    }

    /**
     * Can be overriden to prepare each part of the command line for a given OS.
     */
    protected String osReplace(String elt) {
        return elt;
    }
}
