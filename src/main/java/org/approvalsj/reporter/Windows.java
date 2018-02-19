package org.approvalsj.reporter;

public interface Windows {

    /**
     * Download GVim from http://www.vim.org/download.php
     */
    Reporter GVIM = new ExecReporter("cmd /c gvimdiff %approved% %received%");

    /**
     * Download KDiff3 from https://sourceforge.net/projects/kdiff3/files/
     */
    Reporter KDIFF = new ExecReporter("%programFiles%\\KDiff3\\kdiff3.exe %approved% %received% -o %approved%");


}
