package org.approvalsj.approbation;

import java.nio.file.Path;

public interface Approver {
    void approve(Path approved, Path received);

    Approver GvimApprover = new ExecApprover("cmd /c gvimdiff \"%s\" \"%s\"");
}
