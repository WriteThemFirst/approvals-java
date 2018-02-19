package org.approvalsj.reporter;

import java.nio.file.Path;

public interface Reporter {
    void mismatch(Path approved, Path received) throws Throwable;

    default void missing(Path approved, Path received) throws Throwable{
        mismatch(approved, received);
    }
}
