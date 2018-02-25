package com.github.writethemfirst.approvals.reporters;

import com.github.writethemfirst.approvals.Reporter;

import java.nio.file.Path;

import static com.github.writethemfirst.approvals.utils.FileUtils.silentRead;
import static java.lang.String.format;

public class ThrowsReporter implements Reporter {
    @Override
    public void mismatch(Path approvedPath, Path receivedPath) {
        String approved = silentRead(approvedPath);
        String actual = silentRead(receivedPath);
        String detailMessage = format("expected: <%s> but was: <%s>", approved, actual);
        throw new AssertionError(detailMessage);
    }

    @Override
    public boolean isAvailable() {
        return true;
    }
}
