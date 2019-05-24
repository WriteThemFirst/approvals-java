/*
 * Approvals-Java - Approval testing library for Java. Alleviates the burden of hand-writing assertions.
 * Copyright © 2018 Write Them First!
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.writethemfirst.approvals.reporters;

import com.github.writethemfirst.approvals.Reporter;
import com.github.writethemfirst.approvals.files.ApprovalFiles;
import com.github.writethemfirst.approvals.utils.CommandFinder;
import com.github.writethemfirst.approvals.utils.ExecutableCommand;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import static java.util.Arrays.stream;

/**
 * A reporter which delegates execution to an external command.
 */
public class CommandReporter implements Reporter {
    final String[] arguments;
    private final static String DEFAULT_ARGUMENTS = "%received% %approved%";
    final ExecutableCommand executableCommand;
    private final boolean available;

    public CommandReporter(final ExecutableCommand command, final String... arguments) {
        this.available = command.isAvailable();
        this.executableCommand = command;
        this.arguments = arguments;
    }

    public CommandReporter(final CommandFinder command) {
        this(command, DEFAULT_ARGUMENTS);
    }

    /**
     * Constructs the reporter with a single String of arguments, split on spaces.
     */
    public CommandReporter(final CommandFinder command, final String arguments) {
        this(command, arguments.split(" "));
    }

    private CommandReporter(final CommandFinder command, final String... arguments) {
        this.arguments = arguments;
        final Optional<ExecutableCommand> executableCommand = command.executableCommand();
        if (executableCommand.isPresent()) {
            this.executableCommand = executableCommand.get();
            available = this.executableCommand.isAvailable();
        } else {
            available = false;
            this.executableCommand = null;
        }
    }

    @Override
    public void mismatch(final ApprovalFiles files) {
        try {
            executableCommand.execute(actualArguments(files.approved.toAbsolutePath(), files.received.toAbsolutePath()));
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isAvailable() {
        return available;
    }

    /**
     * Prepares the arguments by substituting %approved% and %received% tags with actual files.
     */
    private String[] actualArguments(final Path approved, final Path received) {
        return stream(arguments)
            .map(elt -> prepareCommandElement(approved, received, elt))
            .toArray(String[]::new);
    }

    private String prepareCommandElement(final Path approved, final Path received, final String elt) {
        return elt
            .replace("%approved%", approved.toString())
            .replace("%received%", received.toString());
    }


}
