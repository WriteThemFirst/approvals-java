package com.github.writethemfirst.approvals;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import static com.github.writethemfirst.approvals.utils.FileUtils.silentRead;
import static com.github.writethemfirst.approvals.utils.FileUtils.silentRemove;
import static java.lang.String.format;
import static java.lang.Thread.currentThread;
import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.newBufferedWriter;
import static java.nio.file.Paths.get;
import static java.util.Arrays.stream;

/**
 * # ApprovalsFiles
 *
 * *Approval Testing* relies on comparing data produced by a *Program Under Tests* and data which has been reviewed and
 * validated by the developer. This validated data is stored in files along with the project, and is used for
 * comparisons with the data produced by the program's execution.
 *
 * Two files are needed for computing *Approval Tests*: an *approved* file, which can (and should) be committed with the
 * source code, containing data validated by the developer, and a *received* file, which temporarly holds the data
 * produced by the *Program Under Tests* execution. Those files are then compared to validate the proper behavior of the
 * program.
 *
 * That `ApprovalsFiles` class can be seen as a companion to a test class since it provides useful methods allowing to
 * read and write both *approved* and *received* files. It is linked to a test class since the produced files will be
 * named after the test class and its methods.
 *
 * An `ApprovalsFiles` object will be automatically created and attached to any created `Approvals` object, but you may
 * choose to instanciate one yourself for accessing and managing the *approved* and *received* files of a particular
 * class.
 *
 * @author mdaviot / aneveux
 * @version 1.0
 */
public class ApprovalsFiles {

    private final Class<?> testClass;
    private final Path folder;


    /**
     * Constructs an `ApprovalsFiles` instance.
     *
     * @param testClass The test class linked to this intance. Created files will contain that class name in their
     *                  path.
     */
    public ApprovalsFiles(final Class<?> testClass) {
        this.testClass = testClass;
        this.folder = folderForClass();
    }


    /**
     * Writes the provided content in the *approved* file linked to the current method execution.
     *
     * That method will actually retrieve the method from which the call has been made and name the *approved* file from
     * the `testClass` attribute, but also the parent method calling this one.
     *
     * If the file doesn't exist, that method will create it in the `src/test/resources` folder.
     *
     * @param content Content to be written in the *approved* file linked to the parent method execution.
     */
    public void writeApproved(final String content) {
        final Path approvedFile = approvedFile();
        write(content, approvedFile);
    }


    public String readApproved() {
        return silentRead(approvedFile());
    }


    public void removeApproved() {
        silentRemove(approvedFile());
    }


    public void writeReceived(String content) {
        Path receivedFile = receivedFile();
        write(content, receivedFile);
    }


    public String readReceived() {
        return silentRead(receivedFile());
    }


    public void removeReceived() {
        silentRemove(receivedFile());
    }


    public Path approvedFile() {
        return approvedFile(methodName().get());
    }


    public Path receivedFile() {
        return receivedFile(methodName().get());
    }


    Path receivedFile(String methodName) {
        String fileName = format("%s.received", methodName);
        return folder.resolve(fileName);
    }


    Path approvedFile(String methodName) {
        String fileName = format("%s.approved", methodName);
        return folder.resolve(fileName);
    }


    /**
     * Gets from the stack the name of the method which was called from the class.
     *
     * @return a method name, or empty if none was found
     */
    public Optional<String> methodName() {
        return stream(currentThread()
            .getStackTrace())
            .filter(e -> e.getClassName()
                .equals(testClass.getName()))
            .filter(e -> !e.getMethodName()
                .startsWith("lambda$"))
            .map(e -> e.getMethodName())
            .findFirst();
    }


    private Path folderForClass() {
        String packageName = testClass.getPackage()
            .getName();
        Path packageResourcesPath = get("src/test/resources/", packageName.split("\\."));
        return packageResourcesPath.resolve(testClass.getSimpleName());
    }


    private void write(String content, Path file) {
        try {
            createDirectories(file.getParent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (BufferedWriter writer = newBufferedWriter(file)) {
            writer.write(content);
        } catch (IOException e) {
            String message = format("Could not write to file %s because of %s", file.toAbsolutePath(), e);
            throw new RuntimeException(message, e);
        }
    }
}
