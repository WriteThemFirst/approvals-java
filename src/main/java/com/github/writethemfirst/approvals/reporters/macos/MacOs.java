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
package com.github.writethemfirst.approvals.reporters.macos;

import com.github.writethemfirst.approvals.reporters.CommandReporterSpec;
import com.github.writethemfirst.approvals.reporters.linux.Linux;

import java.util.Arrays;
import java.util.List;


/**
 * Defines `Reporter`s which work on MacOs only.
 */
public interface MacOs {
    CommandReporterSpec KDIFF_OLD = new CommandReporterSpec("/Applications/kdiff3.app/Contents/MacOS", "kdiff3", "%received% %approved% -o %approved%");
    CommandReporterSpec KDIFF_BREW = new CommandReporterSpec("/opt/homebrew/bin", "kdiff3", "%received% %approved% -o %approved%");
    CommandReporterSpec IDEA_ULTIMATE = new CommandReporterSpec("/Applications/IntelliJ IDEA.app/Contents/MacOS", "idea", "diff %received% %approved%");
    CommandReporterSpec IDEA_COMMUNITY = new CommandReporterSpec("/Applications/IdeaIC.app/Contents/MacOS", "idea", "diff %received% %approved%");
    CommandReporterSpec IDEA_COMMUNITY_TOOLBOX = new CommandReporterSpec("~/Library/Application Support/JetBrains/Toolbox/scripts", "idea", "diff %received% %approved%");
    CommandReporterSpec IDEA_CE = new CommandReporterSpec("/Applications/IntelliJ IDEA CE.app/Contents/MacOS", "idea", "diff %received% %approved%");


    List<CommandReporterSpec> knownCommandReporters = Arrays.asList(
        Linux.MELD, Linux.VS_CODE, Linux.IDEA, Linux.RUBY_MINE,
        IDEA_ULTIMATE, IDEA_COMMUNITY, IDEA_COMMUNITY_TOOLBOX, IDEA_CE,
        KDIFF_BREW, KDIFF_OLD);
}
