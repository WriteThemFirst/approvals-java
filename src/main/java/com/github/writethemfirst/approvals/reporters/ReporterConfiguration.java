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
import com.github.writethemfirst.approvals.utils.FileUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.writethemfirst.approvals.utils.FileUtils.silentRead;
import static java.lang.String.format;
import static java.lang.String.join;

class ReporterConfiguration {
    private static final String commentCharacter = "#";
    private static String separator = "////";
    private static String home = System.getProperty("user.home");
    static Path dotFile = Paths.get(home, ".approvals-java");

    static Optional<CommandReporter> read() {
        if (dotFile.toFile().exists()) {
            try {
                return parse(silentRead(dotFile));
            } catch (RuntimeException e) {
                System.err.println(String.format("Could not parse configuration %s, using defaults", dotFile));
                System.err.println(e);
                return Optional.empty();
            }
        } else {
            write();
            System.out.println(format("Initialized %s, uncomment lines to select your preferred command", dotFile));
            return Optional.empty();
        }
    }

    static Optional<CommandReporter> parse(String configurationContent) {
        return parseReporters(configurationContent)
            .filter(Reporter::isAvailable)
            .findFirst();
    }

    static Stream<CommandReporter> parseReporters(final String configurationContent) {
        final String lineEndingsRegexp = "\n|\r\n";
        return Stream.of(configurationContent.split(lineEndingsRegexp))
            .map(String::trim)
            .filter(line -> !line.startsWith(commentCharacter))
            .map(ReporterConfiguration::parseLine);
    }

    private static CommandReporter parseLine(String line) {
        final String[] split = line.split(separator);
        final String exec = split[0].trim();
        final String[] args = split[1].trim().split(" ");
        return new CommandReporter(new ExecutableCommand(exec), args);
    }

    static void write() {
        SupportedOs.activeOs().ifPresent(os ->
            FileUtils.write(
                os.specs.stream()
                    .flatMap(s ->
                        s.finder().searchForAllExe().map(e ->
                            format("# %s %s %s%n", e, separator, join(" ", s.arguments))))
                    .collect(Collectors.joining("")),
                dotFile
            )
        );

    }
}
