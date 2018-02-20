package com.github.writethemfirst.approvals.reporter;

import static com.github.writethemfirst.approvals.util.FileUtils.silentRead;
import static java.lang.String.format;

import java.nio.file.Path;

public class ThrowsReporter
        implements Reporter {
    @Override
    public void mismatch(Path approvedPath, Path receivedPath)
            throws Throwable {
        String approved = silentRead(approvedPath);
        String actual = silentRead(receivedPath);
        String detailMessage = format("expected: <%s> but was: <%s>", approved, actual);
        throw new AssertionError(detailMessage);
    }
}
