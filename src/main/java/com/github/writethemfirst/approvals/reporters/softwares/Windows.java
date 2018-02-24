package com.github.writethemfirst.approvals.reporters.softwares;

import com.github.writethemfirst.approvals.Reporter;
import com.github.writethemfirst.approvals.reporters.CommandReporter;
import com.github.writethemfirst.approvals.reporters.WindowsReporter;
import com.github.writethemfirst.approvals.reporters.commands.Command;

public interface Windows {

    /**
     * Download GVim from http://www.vim.org/download.php
     */
    Reporter GVIM = new WindowsReporter("cmd /c gvimdiff %approved% %received%");


    Reporter IDEA = new CommandReporter(new Command("%programFiles%\\JetBrains", "idea64.exe"), "merge %approved% %received% %approved%");

    /**
     * Download KDiff3 from https://sourceforge.net/projects/kdiff3/files/
     */
    Reporter KDIFF = new CommandReporter(new Command("%programFiles%\\KDiff3", "kdiff3.exe"), "%approved% %received% -o %approved%");
//    Reporter GVIM = new CommandReporter(new Command("%programFiles%\\Vim", "gvimdiff.exe"), "%approved% %received%");

}



