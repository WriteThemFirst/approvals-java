package com.github.writethemfirst.approvals.reporters;

import com.github.writethemfirst.approvals.AvailableReporter;
import com.github.writethemfirst.approvals.Reporter;

import java.nio.file.Path;
import java.util.Optional;

public class FirstWorkingReporter implements Reporter {
    private final AvailableReporter[] reporters;
    private Optional<AvailableReporter> firstAvailable;

    public FirstWorkingReporter(AvailableReporter... reporters) {
        this.reporters = reporters;
    }

    @Override
    public void mismatch(Path approved, Path received) throws Throwable {
        if (firstAvailable().isPresent()) {
            firstAvailable().get().mismatch(approved, received);
        }
    }

    private Optional<AvailableReporter> firstAvailable() {
        boolean firstTime = firstAvailable == null;
        if (firstTime) {
            firstAvailable = findFirstAvailable();
        }
        return firstAvailable;
    }

    private Optional<AvailableReporter> findFirstAvailable() {
        for (AvailableReporter reporter : reporters) {
            if (reporter.isAvailable()) {
                return Optional.of(reporter);
            }
        }
        return Optional.empty();
    }
}
