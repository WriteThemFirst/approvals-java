package org.approvalsj.reporter;

import org.approvalsj.util.FileUtils;

import java.nio.file.Path;

@FunctionalInterface
public interface MismatchReporter {
    void reportMismatch(Path approved, Path received) throws Throwable;

}
