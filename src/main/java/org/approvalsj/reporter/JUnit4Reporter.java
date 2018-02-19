package org.approvalsj.reporter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;

import static java.lang.String.format;
import static org.approvalsj.util.FileUtils.silentRead;

public class JUnit4Reporter implements Reporter {

    @Override
    public void mismatch(Path approved, Path received) throws Throwable {
        try {
            Class<?> testCaseClass = Class.forName("org.junit.jupiter.api.Assertions");
            Method assertEquals = testCaseClass.getMethod("assertEquals", Object.class, Object.class, String.class);
            assertEquals.invoke(null, silentRead(approved), silentRead(received), format("%s differs from %s", received, approved));
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    @Override
    public void missing(Path approved, Path received) throws Throwable {
        try {
            Class<?> testCaseClass = Class.forName("org.junit.jupiter.api.Assertions");
            Method assertEquals = testCaseClass.getMethod("assertEquals", Object.class, Object.class, String.class);
            String message = format("approved file %s does not exist, rename %s to approve", approved, received);
            assertEquals.invoke(null, "", silentRead(received), message);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }
}
