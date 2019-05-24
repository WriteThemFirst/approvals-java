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

import java.io.IOException;
import java.nio.file.Path;

import static java.lang.String.join;
import static java.util.Arrays.stream;

/**
 * A reporter which delegates execution to an external command.
 */
public class CommandReporter implements Reporter {
    private final Command command;
    private final String[] arguments;
    private final static String DEFAULT_ARGUMENTS = "%received% %approved%";


    public CommandReporter(final Command command) {
        this(command, DEFAULT_ARGUMENTS);
    }

    /**
     * Constructs the reporter with a single String of arguments, split on spaces.
     */
    public CommandReporter(final Command command, final String arguments) {
        this(command, arguments.split(" "));
    }

    private CommandReporter(final Command command, final String... arguments) {
        this.command = command;
        this.arguments = arguments;
    }

    @Override
    public void mismatch(final ApprovalFiles files) {
        if (command.isAvailable()) {
            try {
                command.execute(actualArguments(files.approved.toAbsolutePath(), files.received.toAbsolutePath()));
            } catch (final IOException e) {
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

    @Override
    public String toString() {
        return join(" ", command.buildCommandArray(arguments));
    }
}
