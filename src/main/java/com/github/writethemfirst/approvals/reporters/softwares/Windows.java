package com.github.writethemfirst.approvals.reporters.softwares;

import com.github.writethemfirst.approvals.AvailableReporter;
import com.github.writethemfirst.approvals.Reporter;
import com.github.writethemfirst.approvals.reporters.CommandReporter;
import com.github.writethemfirst.approvals.reporters.FirstWorkingReporter;
import com.github.writethemfirst.approvals.reporters.commands.Command;

public interface Windows {

    AvailableReporter IDEA = new CommandReporter(new Command("%programFiles%\\JetBrains", "idea64.exe"), "merge %approved% %received% %approved%");

    /**
     * Download KDiff3 from https://sourceforge.net/projects/kdiff3/files/
     */
    AvailableReporter KDIFF = new CommandReporter(new Command("%programFiles%\\KDiff3", "kdiff3.exe"), "%approved% %received% -o %approved%");

    /**
     * Download GVim from http://www.vim.org/download.php
     */
    AvailableReporter GVIM = new CommandReporter(new Command("%programFiles%\\Vim", "gvim.exe"), "-d %approved% %received% %received%");

    Reporter DEFAULT = new FirstWorkingReporter(IDEA, KDIFF, GVIM);
}
