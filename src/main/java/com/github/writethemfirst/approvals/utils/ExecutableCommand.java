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
package com.github.writethemfirst.approvals.utils;

import java.io.File;
import java.io.IOException;

import static java.lang.String.join;
import static java.nio.file.Paths.get;
import static java.util.Arrays.stream;
import static java.util.stream.Stream.concat;
import static java.util.stream.Stream.of;

/**
 * Wrapper around an executable command outside the JVM.
 */
public class ExecutableCommand {
    private final Runtime runtime;
    private final String executable;

    /**
     * Only use this constructor from test code so the environment Map and Runtime can be mocked.
     */
    ExecutableCommand(final String executable, final Runtime runtime) {
        this.executable = executable;
        this.runtime = runtime;
    }

    /**
     * Runs the executable outside the JVM by calling Runtime.exec().
     */
    public void execute(final String... arguments) throws IOException {
        final String[] cmdArray = buildCommandArray(arguments);
        System.out.printf("Running command [%s]%n", join(" ", cmdArray));

        try {
            runtime.exec(cmdArray).waitFor();
        } catch (final InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private String[] buildCommandArray(final String[] arguments) {
        return concat(
            of(executable),
            stream(arguments))
            .toArray(String[]::new);
    }

    /**
     * Tests if an executable file was found in the path.
     */
    public boolean isAvailable() {
        final File file = get(executable).toFile();
        return file.exists() && file.canExecute();
    }

}
