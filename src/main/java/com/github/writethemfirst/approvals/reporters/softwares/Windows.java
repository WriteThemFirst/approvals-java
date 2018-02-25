package com.github.writethemfirst.approvals.reporters.softwares;

import com.github.writethemfirst.approvals.Reporter;
import com.github.writethemfirst.approvals.reporters.CommandReporter;
import com.github.writethemfirst.approvals.reporters.FirstWorkingReporter;
import com.github.writethemfirst.approvals.reporters.JUnit5Reporter;
import com.github.writethemfirst.approvals.reporters.ThrowsReporter;
import com.github.writethemfirst.approvals.reporters.commands.Command;


/**
 * Defines `Reporter`s which work on Windows only.
 */
public interface Windows {

    Reporter IDEA = new CommandReporter(new Command("%programFiles%\\JetBrains", "idea64.exe"), "merge %approved% %received% %approved%");

    /**
     * Download KDiff3 from https://sourceforge.net/projects/kdiff3/files/
     */
    Reporter KDIFF = new CommandReporter(new Command("%programFiles%\\KDiff3", "kdiff3.exe"), "%approved% %received% -o %approved%");

    /**
     * Download GVim from http://www.vim.org/download.php
     */
    Reporter GVIM = new CommandReporter(new Command("%programFiles%\\Vim", "gvim.exe"), "-d %approved% %received% %received%");

    Reporter DEFAULT = new FirstWorkingReporter(IDEA, KDIFF, GVIM, new JUnit5Reporter(), new ThrowsReporter());
}
