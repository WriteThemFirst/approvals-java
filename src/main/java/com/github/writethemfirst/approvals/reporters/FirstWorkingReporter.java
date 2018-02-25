package com.github.writethemfirst.approvals.reporters;

import com.github.writethemfirst.approvals.Reporter;

import java.nio.file.Path;
import java.util.Optional;

public class FirstWorkingReporter implements Reporter {
    private final Reporter[] reporters;
    private Optional<Reporter> firstAvailable;

    public FirstWorkingReporter(Reporter... reporters) {
        this.reporters = reporters;
    }

    @Override
    public void mismatch(Path approved, Path received) throws Throwable {
        if (firstAvailable().isPresent()) {
            firstAvailable().get().mismatch(approved, received);
        }
    }

    @Override
    public boolean isAvailable() {
        return firstAvailable().isPresent();
    }

    private Optional<Reporter> firstAvailable() {
        boolean firstTime = firstAvailable == null;
        if (firstTime) {
            firstAvailable = findFirstAvailable();
        }
        return firstAvailable;
    }

    private Optional<Reporter> findFirstAvailable() {
        for (Reporter reporter : reporters) {
            if (reporter.isAvailable()) {
                return Optional.of(reporter);
            }
        }
        return Optional.empty();
    }
}
