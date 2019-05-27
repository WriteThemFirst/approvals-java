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

import com.github.writethemfirst.approvals.utils.CommandFinder;

import java.util.stream.Collectors;

import static java.util.Arrays.stream;

/**
 * Describes a CommandReporter : in which folder to find it, the name of the exe, the format of the arguments
 */
public class CommandReporterSpec {
    final String folder;
    final String arguments;
    final String executableName;
    static final String DEFAULT_ARGUMENTS = "%received% %approved%";

    public CommandReporterSpec(final String folder, final String executableName) {
        this(folder, executableName, DEFAULT_ARGUMENTS);
    }

    public CommandReporterSpec(final String folder, final String executableName, final String... arguments) {
        this.folder = folder;
        this.arguments = stream(arguments).collect(Collectors.joining(" "));
        this.executableName = executableName;
    }

    public CommandReporter reporter() {
        return new CommandReporter(finder(), arguments);
    }

    public CommandFinder finder() {
        return new CommandFinder(folder, executableName);
    }
}
