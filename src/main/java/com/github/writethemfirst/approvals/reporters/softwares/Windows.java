package com.github.writethemfirst.approvals.reporters.softwares;

import com.github.writethemfirst.approvals.Reporter;
import com.github.writethemfirst.approvals.reporters.WindowsReporter;

public interface Windows {

    /**
     * Download GVim from http://www.vim.org/download.php
     */
    Reporter GVIM = new WindowsReporter("cmd /c gvimdiff %approved% %received%");

    /**
     * Download KDiff3 from https://sourceforge.net/projects/kdiff3/files/
     */
    Reporter KDIFF = new WindowsReporter("%programFiles%\\KDiff3\\kdiff3.exe %approved% %received% -o %approved%");

    //TODO: make it aware of other versions of IDEA
    Reporter IDEA = new WindowsReporter("%programFiles%\\JetBrains\\IntelliJ IDEA 2017.3\\bin\\idea64.exe", "merge", "%approved%", "%received%", "%approved%");


}



