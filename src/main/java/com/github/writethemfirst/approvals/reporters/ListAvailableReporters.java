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
import com.github.writethemfirst.approvals.utils.FileUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;


public class ListAvailableReporters {
    static String separator = "////";
    static String home = System.getProperty("user.home");
    static Path dotFile = Paths.get(home, ".approvals-java");

    public static void main(String[] args) {
        write();
        //read();

    }


    private static void write() {
        SupportedOs.activeOs().ifPresent(os ->
            FileUtils.write(
                os.possibleReporters.stream()
                    .filter(Reporter::isAvailable)
                    .map(r -> String.format("# %s %s %s%n", r.executableCommand.executable, separator, String.join(" ", r.arguments)))
                    .collect(Collectors.joining("")
                    ),
                dotFile
            )
        );

    }

}
