package org.approvalsj.reporter;

import org.approvalsj.util.FileUtils;

import java.nio.file.Path;

import static java.lang.String.format;

public class ThrowsReporter implements Reporter {
    @Override
    public void mismatch(Path approvedPath, Path receivedPath) throws Throwable {
        String approved = FileUtils.silentRead(approvedPath);
        String actual = FileUtils.silentRead(receivedPath);
        String detailMessage = format("expected: <%s> but was: <%s>", approved, actual);
        throw new AssertionError(detailMessage);
    }

    @Override
    public void missing(Path approved, Path received) throws Throwable {
        throw new AssertionError(approved + " does not exist yet");
    }
}
