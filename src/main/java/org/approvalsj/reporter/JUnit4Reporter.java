package org.approvalsj.reporter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;

import static org.approvalsj.util.FileUtils.silentRead;

public class JUnit4Reporter implements Reporter {

    @Override
    public void mismatch(Path approved, Path received) throws Throwable {
        try {
            Class<?> testCaseClass = Class.forName("org.junit.jupiter.api.Assertions");
            Method assertEquals = testCaseClass.getMethod("assertEquals", Object.class, Object.class);
            assertEquals.invoke(null, silentRead(approved), silentRead(received));
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }
}
