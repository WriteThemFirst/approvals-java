package com.github.writethemfirst.approvals;

import java.nio.file.Path;

public interface Reporter {
    void mismatch(Path approved, Path received) throws Throwable;

    boolean isAvailable();
}
