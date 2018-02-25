package com.github.writethemfirst.approvals.reporters;

import com.github.writethemfirst.approvals.Reporter;
import com.github.writethemfirst.approvals.reporters.commands.Command;

import java.io.IOException;
import java.nio.file.Path;

import static java.util.Arrays.stream;

/**
 * A reporter which delegates execution to an external command.
 */
public class CommandReporter implements Reporter {
    private final Command command;
    private final String[] arguments;

    /**
     * Constructs the reporter with a single String of arguments, split on spaces.
     */
    public CommandReporter(Command command, String arguments) {
        this(command, arguments.split(" "));
    }

    private CommandReporter(Command command, String... arguments) {
        this.command = command;
        this.arguments = arguments;
    }

    @Override
    public void mismatch(Path approved, Path received) {
        if (command.isAvailable()) {
            try {
                command.execute(actualArguments(approved, received));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public boolean isAvailable() {
        return command.isAvailable();
    }


    /**
     * Prepares the arguments by substituting %approved% and %received% tags with actual files.
     */
    String[] actualArguments(Path approved, Path received) {
        return stream(arguments)
            .map(elt -> prepareCommandElement(approved, received, elt))
            .toArray(String[]::new);
    }

    private String prepareCommandElement(Path approved, Path received, String elt) {
        return elt
            .replace("%approved%", approved.toString())
            .replace("%received%", received.toString());
    }
}
