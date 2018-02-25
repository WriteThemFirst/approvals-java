package com.github.writethemfirst.approvals.reporters;

import com.github.writethemfirst.approvals.AvailableReporter;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FirstWorkingReporterTest {
    @Test
    void shouldUseFirstWorkingReporter() throws Throwable {
        AvailableReporter reporter1 = mock(AvailableReporter.class);
        AvailableReporter reporter2 = mock(AvailableReporter.class);
        AvailableReporter reporter3 = mock(AvailableReporter.class);
        Path a = Paths.get("a");
        Path b = Paths.get("b");
        when(reporter1.isAvailable()).thenReturn(false);
        when(reporter2.isAvailable()).thenReturn(true);
        when(reporter3.isAvailable()).thenReturn(true);

        FirstWorkingReporter firstWorkingReporter = new FirstWorkingReporter(reporter1, reporter2, reporter3);

        firstWorkingReporter.mismatch(a, b);

        then(reporter2).should().mismatch(a, b);

    }

    @Test
    void shouldIgnoreWhenNoReporterIsAvailable() throws Throwable {
        AvailableReporter reporter1 = mock(AvailableReporter.class);
        Path a = Paths.get("a");
        Path b = Paths.get("b");
        when(reporter1.isAvailable()).thenReturn(false);

        FirstWorkingReporter firstWorkingReporter = new FirstWorkingReporter(reporter1);

        firstWorkingReporter.mismatch(a, b); // should not throw
    }

}
