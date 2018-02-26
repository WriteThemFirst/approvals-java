package com.github.writethemfirst.approvals.reporters;

import com.github.writethemfirst.approvals.Reporter;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FirstWorkingReporterTest {
    @Test
    void shouldUseFirstWorkingReporter() {
        Reporter reporter1 = mock(Reporter.class);
        Reporter reporter2 = mock(Reporter.class);
        Reporter reporter3 = mock(Reporter.class);
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
    void shouldIgnoreWhenNoReporterIsAvailable() {
        Reporter reporter1 = mock(Reporter.class);
        Path a = Paths.get("a");
        Path b = Paths.get("b");
        when(reporter1.isAvailable()).thenReturn(false);

        FirstWorkingReporter firstWorkingReporter = new FirstWorkingReporter(reporter1);

        firstWorkingReporter.mismatch(a, b); // should not throw
    }

}
