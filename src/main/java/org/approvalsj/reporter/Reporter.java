package org.approvalsj.reporter;

import java.nio.file.Path;

public interface Reporter {
    void mismatch(Path approved, Path received) throws Throwable;
}
