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

import com.github.writethemfirst.approvals.reporters.CommandReporter;
import com.github.writethemfirst.approvals.reporters.CommandReporterSpec;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Defines `Reporter`s which work on Windows only.
 */
public interface Windows {

    CommandReporterSpec IDEA = new CommandReporterSpec("%programFiles%\\JetBrains", "idea64.exe", "diff %received% %approved%");
    /**
     * Download KDiff3 from https://sourceforge.net/projects/kdiff3/files/
     */
    CommandReporterSpec KDIFF = new CommandReporterSpec("%programFiles%\\KDiff3", "kdiff3.exe", "%received% %approved% -o %approved%");

    /**
     * Download GVim from http://www.vim.org/download.php
     */
    CommandReporterSpec GVIM = new CommandReporterSpec("%programFiles%\\Vim", "gvim.exe", "-d %approved% %received% %received%");

    /**
     * Download TortoiseSVN from https://tortoisesvn.net/downloads.html
     */
    CommandReporterSpec TORTOISE_SVN = new CommandReporterSpec("%programFiles%\\TortoiseSVN", "TortoiseMerge.exe");

    CommandReporterSpec BEYOND_COMPARE_3 = new CommandReporterSpec("%programFiles%\\Beyond Compare 3", "BCompare.exe");
    CommandReporterSpec WINMERGE = new CommandReporterSpec("%programFiles%\\WinMerge", "WinMergeU.exe");
    CommandReporterSpec ARAXIS = new CommandReporterSpec("%programFiles%\\WinMerge", "WinMergeU.exe");
    CommandReporterSpec CODE_COMPARE = new CommandReporterSpec("%programFiles%\\Devart", "CodeCompare.exe");


    List<CommandReporterSpec> knownCommandReporters = Arrays.asList(KDIFF, IDEA, TORTOISE_SVN,  BEYOND_COMPARE_3, WINMERGE, ARAXIS, CODE_COMPARE, GVIM);

    List<CommandReporter> possibleNativeReporters = knownCommandReporters.stream().map(CommandReporterSpec::reporter).collect(Collectors.toList());

}
