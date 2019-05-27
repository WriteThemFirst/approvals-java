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

import com.github.writethemfirst.approvals.Reporter;
import com.github.writethemfirst.approvals.reporters.CommandReporter;
import com.github.writethemfirst.approvals.reporters.FirstWorkingReporter;
import com.github.writethemfirst.approvals.reporters.JUnit5Reporter;
import com.github.writethemfirst.approvals.reporters.ThrowsReporter;
import com.github.writethemfirst.approvals.utils.CommandFinder;


/**
 * Defines `Reporter`s which work on MacOs only.
 */
public interface MacOs {

    CommandReporter KDIFF = new CommandReporter(new CommandFinder(
        "/Applications/kdiff3.app/Contents/MacOS",
        "kdiff3"),
        "%received% %approved% -o %approved%");

    CommandReporter IDEA_ULTIMATE = new CommandReporter(new CommandFinder(
        "/Applications/IntelliJ IDEA.app/Contents/MacOS",
        "idea"),
        "diff %received% %approved%");

    CommandReporter IDEA_COMMUNITY = new CommandReporter(new CommandFinder(
        "/Applications/IdeaIC.app/Contents/MacOS",
        "idea"),
        "diff %received% %approved%");

    CommandReporter IDEA_CE = new CommandReporter(new CommandFinder(
        "/Applications/IntelliJ IDEA CE.app/Contents/MacOS",
        "idea"),
        "diff %received% %approved%");

    CommandReporter IDEA = new CommandReporter(new CommandFinder(
        "/usr/local/bin",
        "idea"),
        "diff %received% %approved%");

    Reporter DEFAULT = new FirstWorkingReporter(
        IDEA,
        IDEA_ULTIMATE,
        IDEA_COMMUNITY,
        IDEA_CE,
        KDIFF,
        new JUnit5Reporter(),
        new ThrowsReporter());
}
