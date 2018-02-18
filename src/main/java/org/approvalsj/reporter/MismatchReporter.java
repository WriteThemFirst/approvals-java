package org.approvalsj.reporter;

import java.nio.file.Path;

@FunctionalInterface
public interface MismatchReporter {
    void reportMismatch(Path approved, Path received);
}
