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
import com.github.writethemfirst.approvals.utils.ExecutableCommand;

import java.util.Optional;
import java.util.stream.Stream;

public class ReporterConfiguration {
    static final String commentCharacter = "#";
    static String lineEndingsRegexp = "\n|\r\n";
    static String separator = "////";


    public static Optional<CommandReporter> read(String configurationContent) {
        return parseReporters(configurationContent)
            .filter(Reporter::isAvailable)
            .findFirst();
    }

     static Stream<CommandReporter> parseReporters(final String configurationContent) {
        return Stream.of(configurationContent.split(lineEndingsRegexp))
            .map(String::trim)
            .filter(line -> !line.startsWith(commentCharacter))
            .map(ReporterConfiguration::parseLine);
    }

    static CommandReporter parseLine(String line) {
        final String[] split = line.split(separator);
        final String exec = split[0].trim();
        final String[] args = split[1].trim().split(" ");
        return new CommandReporter(new ExecutableCommand(exec), args);
    }
}
