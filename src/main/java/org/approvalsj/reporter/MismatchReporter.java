package org.approvalsj.reporter;

import java.nio.file.Path;

public interface MismatchReporter {
    void reportMismatch(Path approved, Path received) throws Throwable;

    default void reportMissing(Path approved, Path received) throws Throwable{
        reportMismatch(approved, received);
    }
}
