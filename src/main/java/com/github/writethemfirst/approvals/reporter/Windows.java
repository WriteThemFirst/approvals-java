package com.github.writethemfirst.approvals.reporter;

public interface Windows {

    /**
     * Download GVim from http://www.vim.org/download.php
     */
    Reporter GVIM = new ExecReporter("cmd /c gvimdiff %approved% %received%");

    /**
     * Download KDiff3 from https://sourceforge.net/projects/kdiff3/files/
     */
    Reporter KDIFF = new ExecReporter("%programFiles%\\KDiff3\\kdiff3.exe %approved% %received% -o %approved%");

    //TODO: make it aware of other versions of IDEA
    Reporter IDEA = new ExecReporter("%programFiles%\\JetBrains\\IntelliJ IDEA 2017.3\\bin\\idea64.exe", "merge", "%approved%", "%received%", "%approved%");


}
