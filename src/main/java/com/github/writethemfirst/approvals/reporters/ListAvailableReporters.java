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

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.writethemfirst.approvals.reporters.windows.Windows.*;

public class ListAvailableReporters {
    static String separator = "////";
    static String home = System.getProperty("user.home");
    static Path dotFile = Paths.get(home, ".approvals-java");

    public static void main(String[] args) {
        //write();
        read();

    }

    private static void read() {
        String lines2 = FileUtils.silentRead(dotFile);
        Stream.of(lines2.split("\n|\r\n"))
            .map(String::trim)
            .filter(line -> !line.startsWith("#"))
            .map(ListAvailableReporters::parseLine)
            .filter(CommandReporter::isAvailable)
            .map(r -> String.format("# %s %s %s", r.executableCommand.executable, separator, String.join(" ", r.arguments)))
            .forEach(System.out::println);
    }

    private static void write() {
        String lines = Stream.of(KDIFF,
            IDEA,
            TORTOISE_SVN,
            BEYOND_COMPARE_4,
            BEYOND_COMPARE_3,
            WINMERGE,
            ARAXIS,
            CODE_COMPARE,
            GVIM)
            .filter(Reporter::isAvailable)
            .map(r -> String.format("# %s %s %s%n", r.executableCommand.executable, separator, String.join(" ", r.arguments)))
            .collect(Collectors.joining(""));
        FileUtils.write(lines, dotFile);
    }

    static CommandReporter parseLine(String line) {
        final String[] split = line.split(separator);
        final String exec = split[0].trim();
        final String[] args = split[1].trim().split(" ");
        return new CommandReporter(new ExecutableCommand(exec), args);

    }
}
