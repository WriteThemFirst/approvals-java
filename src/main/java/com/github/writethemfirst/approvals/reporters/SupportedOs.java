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
import com.github.writethemfirst.approvals.reporters.linux.Linux;
import com.github.writethemfirst.approvals.reporters.macos.MacOs;
import com.github.writethemfirst.approvals.reporters.windows.Windows;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.github.writethemfirst.approvals.reporters.ReporterConfiguration.dotFile;
import static com.github.writethemfirst.approvals.reporters.ReporterConfiguration.read;

/**
 * # SupportedOs
 *
 * Approvals-Java can be executed from all kind of environments, which also means all kind of OS (at least Windows,
 * Linux, Mac OSX).
 *
 * That means some operations will have to be handled in different ways depending on which Operating System it is
 * executed on.
 */
public enum SupportedOs {
    WINDOWS("windows", Windows.knownCommandReporters),
    MAC_OS("mac", MacOs.knownCommandReporters),
    LINUX("linux", Linux.knownCommandReporters);

    public final List<CommandReporterSpec> specs;
    public final boolean active;

    SupportedOs(final String prefix, final List<CommandReporterSpec> specs) {
        this.specs = specs;
        final String osName = System.getProperty("os.name").toLowerCase();
        active = osName.startsWith(prefix);
    }

    /**
     * Identifies the current OS and the associated {@link Reporter}.
     *
     * @return the default Reporter (which will run a native diff tool) for the OS
     */
    public static Optional<Reporter> osDefaultReporter() {
        return activeOs().map(SupportedOs::defaultReporter);
    }

    Reporter defaultReporter() {
        final Optional<CommandReporter> configuredReporter = read();
        if (configuredReporter.isPresent()) {
            System.out.printf("Using reporter configured in %s : %s%n", dotFile, configuredReporter.get());
            return configuredReporter.get();
        } else {
            FirstWorkingReporter first = new FirstWorkingReporter(specs.stream().map(CommandReporterSpec::reporter).toArray(Reporter[]::new));
            if(first.firstWorking().isPresent()) {
                Reporter reporter = first.firstWorking().get();
                System.out.printf("No reporter configured in %s, using %s%n", dotFile, reporter);
            } else {
                System.out.printf("No reporter configured in %s and no reporter supported on this platform, likely a bug in approvals%n", dotFile);
            }
            return first;
        }
    }

    /**
     * Identifies the current OS and the associated {@link Reporter}.
     *
     * @return the default Reporter (which will run a native diff tool) for the OS
     */
    public static Optional<SupportedOs> activeOs() {
        return Arrays
            .stream(values())
            .filter(os -> os.active)
            .findFirst();
    }
}
