package org.approvalsj.reporter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;

import static org.approvalsj.util.FileUtils.silentRead;

//TODO : needs to be tested
public class JUnit3Reporter implements MismatchReporter {

    @Override
    public void reportMismatch(Path approved, Path received) throws Throwable {
        try {
            Class<?> testCaseClass = Class.forName("junit.framework.TestCase");
            Method assertEquals = testCaseClass.getMethod("assertEquals", String.class, String.class);
            assertEquals.invoke(null, silentRead(approved), silentRead(received));
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }
}
