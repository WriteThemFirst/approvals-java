package org.approvalsj.approbation;

import java.nio.file.Path;

public interface Approver {
    void approve(Path approved, Path received);


    /**
     * Download GVim from http://www.vim.org/download.php
     */
    Approver GvimApprover = new ExecApprover("cmd /c gvimdiff %approved% %received%");

    /**
     * Download KDiff3 from https://sourceforge.net/projects/kdiff3/files/
     */
    Approver Kdiff3Approver = new ExecApprover("%programFiles%\\KDiff3\\kdiff3.exe %approved% %received% -o %approved%");
}
