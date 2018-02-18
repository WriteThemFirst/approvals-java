package org.approvalsj.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.String.format;
import static java.nio.file.Files.delete;

public class FileUtils {
    private final Class<?> aClass;
    private final Path folder;

    public FileUtils(Class<?> aClass) {
        this.aClass = aClass;
        this.folder = folder();
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
        return approvedFile(new StackUtils(aClass).methodName().get());
    }

    public Path receivedFile() {
        return receivedFile(new StackUtils(aClass).methodName().get());
    }

    Path receivedFile(String methodName) {
        String fileName = format("%s.received", methodName);
        return folder.resolve(fileName);
    }

    Path approvedFile(String methodName) {
        String fileName = format("%s.approved", methodName);
        return folder.resolve(fileName);
    }

    private String silentRead(Path file) {
        try {
            return new String(Files.readAllBytes(file));
        } catch (IOException e) {
            System.err.println("Could not read from " + file);
            return null;
        }
    }

    private void silentRemove(Path path) {
        try {
            delete(path);
        } catch (IOException e) {
            // we were cleaning just in case
        }
    }

    private Path folder() {
        String packageName = aClass.getPackage().getName();
        Path packageResourcesPath = Paths.get("src/test/resources/", packageName.split("\\."));
        return packageResourcesPath.resolve(aClass.getSimpleName());
    }

    private void write(String content, Path file) {
        try {
            Files.createDirectories(file.getParent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (BufferedWriter writer = Files.newBufferedWriter(file)) {
            writer.write(content);
        } catch (IOException e) {
            String message = format("Could not write to file %s because of %s", file.toAbsolutePath(), e);
            throw new RuntimeException(message, e);
        }
    }
}
