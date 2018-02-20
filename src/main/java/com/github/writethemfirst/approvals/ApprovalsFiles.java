package com.github.writethemfirst.approvals;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;

import static com.github.writethemfirst.approvals.utils.FileUtils.silentRead;
import static com.github.writethemfirst.approvals.utils.FileUtils.silentRemove;
import static java.lang.String.format;
import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.newBufferedWriter;

public class ApprovalsFiles {
    private final Class<?> testClass;
    private final Path folder;


    public ApprovalsFiles(Class<?> testClass) {
        this.testClass = testClass;
        this.folder = folderForClass();
    }


    public void writeApproved(String content) {
        Path approvedFile = approvedFile();
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
        return Arrays.stream(Thread.currentThread()
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
        Path packageResourcesPath = Paths.get("src/test/resources/", packageName.split("\\."));
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
