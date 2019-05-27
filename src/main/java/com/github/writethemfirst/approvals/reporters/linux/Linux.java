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
import com.github.writethemfirst.approvals.utils.CommandFinder;


/**
 * Defines `Reporter`s which work on Linux only.
 */
public interface Linux {

    CommandReporter IDEA_ULTIMATE = new CommandReporter(new CommandFinder(
        "/usr/bin",
        "intellij-idea-ultimate-edition"),
        "diff %received% %approved%");

    CommandReporter IDEA_COMMUNITY = new CommandReporter(new CommandFinder(
        "/usr/bin",
        "idea.sh"),
        "diff %received% %approved%");

    CommandReporter BEYOND_COMPARE = new CommandReporter(new CommandFinder(
        "/usr/bin",
        "bcompare"),
        "%received% %approved%");

    CommandReporter IDEA = new CommandReporter(new CommandFinder(
        "/usr/local/bin",
        "idea"),
        "diff %received% %approved%");

    CommandReporter[] possibleNativeReporters = new CommandReporter[]{
        IDEA,
        IDEA_ULTIMATE,
        IDEA_COMMUNITY,
        BEYOND_COMPARE
    };

}
