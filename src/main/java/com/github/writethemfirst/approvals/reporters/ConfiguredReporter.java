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
import com.github.writethemfirst.approvals.utils.ExecutableCommand;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * # ConfiguredReporter
 *
 * Configures a Reporter from the file `~/.approvals-java`.
 */
public class ConfiguredReporter implements Reporter {
    final Reporter delegate;
    static String separator = "////";

    ConfiguredReporter(String configurationContent) {
        delegate = read(configurationContent).orElse(Reporter.BASIC);
    }


    @Override
    public void mismatch(final ApprovalFiles files) {
        delegate.mismatch(files);
    }

    @Override
    public boolean isAvailable() {
        return delegate.isAvailable();
    }


    private static Optional<Reporter> read(String lines2) {
        return Stream.of(lines2.split("\n|\r\n"))
            .map(String::trim)
            .filter(line -> !line.startsWith("#"))
            .map(ConfiguredReporter::parseLine)
            .filter(Reporter::isAvailable)
            .findFirst();
    }

    static Reporter parseLine(String line) {
        final String[] split = line.split(separator);
        final String exec = split[0].trim();
        final String[] args = split[1].trim().split(" ");
        return new CommandReporter(new ExecutableCommand(exec), args);

    }
}
