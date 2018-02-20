package com.github.writethemfirst.approvals.reporter;

import java.nio.file.Path;

public interface Reporter {
    void mismatch(Path approved, Path received) throws Throwable;
}
