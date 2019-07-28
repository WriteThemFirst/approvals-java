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
package com.github.writethemfirst.approvals.reporters.linux;

import com.github.writethemfirst.approvals.reporters.CommandReporter;
import com.github.writethemfirst.approvals.reporters.CommandReporterSpec;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Defines `Reporter`s which work on Linux only.
 */
public interface Linux {
    CommandReporterSpec IDEA = new CommandReporterSpec("/usr/local/bin", "idea", "diff %received% %approved%");
    CommandReporterSpec MELD = new CommandReporterSpec("/usr/local/bin", "meld", "%received% %approved%");
    CommandReporterSpec IDEA_ULTIMATE = new CommandReporterSpec("/usr/bin", "intellij-idea-ultimate-edition", "diff %received% %approved%");
    CommandReporterSpec IDEA_COMMUNITY = new CommandReporterSpec("/usr/bin", "idea.sh", "diff %received% %approved%");
    CommandReporterSpec BEYOND_COMPARE = new CommandReporterSpec("/usr/bin", "bcompare");
    CommandReporterSpec KDIFF3 = new CommandReporterSpec("/usr/bin", "kdiff3", "%received% %approved% -o %approved%");

    List<CommandReporterSpec> knownCommandReporters = Arrays.asList(MELD, IDEA, IDEA_ULTIMATE, IDEA_COMMUNITY, KDIFF3, BEYOND_COMPARE);

    List<CommandReporter> possibleNativeReporters = knownCommandReporters.stream().map(CommandReporterSpec::reporter).collect(Collectors.toList());


}
