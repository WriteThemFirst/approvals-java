package com.github.writethemfirst.approvals.reporters;

import com.github.writethemfirst.approvals.AvailableReporter;
import com.github.writethemfirst.approvals.reporters.commands.Command;

import java.nio.file.Path;

import static java.util.Arrays.stream;

/**
 * A reporter which executes an external command.
 */
public class CommandReporter implements AvailableReporter {
    private final Command command;
    private final String[] arguments;

    public CommandReporter(Command command, String arguments) {
        this(command, arguments.split(" "));
    }

    private CommandReporter(Command command, String... arguments) {
        this.command = command;
        this.arguments = arguments;
    }

    @Override
    public void mismatch(Path approved, Path received) throws Throwable {
        if (command.isAvailable()) {
            command.execute(actualArguments(approved, received));
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
