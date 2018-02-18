package org.approvalsj.reporter;

import java.nio.file.Path;

@FunctionalInterface
public interface MismatchReporter {
    void reportMismatch(Path approved, Path received);


    /**
     * Download GVim from http://www.vim.org/download.php
     */
    MismatchReporter GVIM = new ExecReporter("cmd /c gvimdiff %approved% %received%");

    /**
     * Download KDiff3 from https://sourceforge.net/projects/kdiff3/files/
     */
    MismatchReporter KDIFF = new ExecReporter("%programFiles%\\KDiff3\\kdiff3.exe %approved% %received% -o %approved%");
}
