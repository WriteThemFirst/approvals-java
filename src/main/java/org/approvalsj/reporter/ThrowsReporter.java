package org.approvalsj.reporter;

import static java.lang.String.format;

import java.nio.file.Path;
import org.approvalsj.util.FileUtils;

public class ThrowsReporter implements Reporter {
    @Override
    public void mismatch(Path approvedPath, Path receivedPath) throws Throwable {
        String approved = FileUtils.silentRead(approvedPath);
        String actual = FileUtils.silentRead(receivedPath);
        String detailMessage = format("expected: <%s> but was: <%s>", approved, actual);
        throw new AssertionError(detailMessage);
    }
}
