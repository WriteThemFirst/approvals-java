package com.github.writethemfirst.approvals.reporters;

import com.github.writethemfirst.approvals.Reporter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;

import static com.github.writethemfirst.approvals.utils.FileUtils.silentRead;
import static java.lang.String.format;

public class JUnit5Reporter implements Reporter {

    private final String JUNIT5_ASSERTIONS = "org.junit.jupiter.api.Assertions";

    @Override
    public void mismatch(Path approved, Path received) throws Throwable {
        try {
            Class<?> testCaseClass = Class.forName(JUNIT5_ASSERTIONS);
            Method assertEquals = testCaseClass.getMethod("assertEquals", Object.class, Object.class, String.class);
            assertEquals.invoke(null,
                silentRead(approved),
                silentRead(received),
                format("%s differs from %s", received, approved));
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    @Override
    public boolean isAvailable() {
        try {
            Class.forName(JUNIT5_ASSERTIONS);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

}
