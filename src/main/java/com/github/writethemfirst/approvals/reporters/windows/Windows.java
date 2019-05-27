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
package com.github.writethemfirst.approvals.reporters.windows;

import com.github.writethemfirst.approvals.Reporter;
import com.github.writethemfirst.approvals.reporters.CommandReporter;
import com.github.writethemfirst.approvals.reporters.FirstWorkingReporter;
import com.github.writethemfirst.approvals.utils.CommandFinder;

import static java.util.stream.Stream.of;


/**
 * Defines `Reporter`s which work on Windows only.
 */
public interface Windows {

    CommandReporter IDEA = new CommandReporter(new CommandFinder(
        "%programFiles%\\JetBrains",
        "idea64.exe"),
        "diff %received% %approved%");

    /**
     * Download KDiff3 from https://sourceforge.net/projects/kdiff3/files/
     */
    CommandReporter KDIFF = new CommandReporter(new CommandFinder(
        "%programFiles%\\KDiff3",
        "kdiff3.exe"),
        "%received% %approved% -o %approved%");

    /**
     * Download GVim from http://www.vim.org/download.php
     */
    CommandReporter GVIM = new CommandReporter(new CommandFinder(
        "%programFiles%\\Vim",
        "gvim.exe"),
        "-d %approved% %received% %received%");

    /**
     * Download TortoiseSVN from https://tortoisesvn.net/downloads.html
     */
    CommandReporter TORTOISE_SVN = new CommandReporter(new CommandFinder(
        "%programFiles%\\TortoiseSVN",
        "TortoiseMerge.exe"));

    CommandReporter BEYOND_COMPARE_3 = new CommandReporter(new CommandFinder(
        "%programFiles%\\Beyond Compare 3",
        "BCompare.exe"));

    CommandReporter BEYOND_COMPARE_4 = new CommandReporter(new CommandFinder(
        "%programFiles%\\Beyond Compare 3",
        "BCompare.exe"));

    CommandReporter WINMERGE = new CommandReporter(new CommandFinder(
        "%programFiles%\\WinMerge",
        "WinMergeU.exe"));

    CommandReporter ARAXIS = new CommandReporter(new CommandFinder(
        "%programFiles%\\Araxis",
        "Compare.exe"));

    CommandReporter CODE_COMPARE = new CommandReporter(new CommandFinder(
        "%programFiles%\\Devart",
        "CodeCompare.exe"),
        "%received% %approved%");


    CommandReporter[] possibleNativeReporters = new CommandReporter[]{
        KDIFF, IDEA, TORTOISE_SVN, BEYOND_COMPARE_4, BEYOND_COMPARE_3, WINMERGE, ARAXIS, CODE_COMPARE, GVIM
    };

}
