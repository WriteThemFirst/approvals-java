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
        try {
            Files.createDirectories(approvedFile.getParent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (BufferedWriter writer = Files.newBufferedWriter(approvedFile)) {
            writer.write(content);
        } catch (IOException e) {
            String message = format("Could not write to file %s because of %s", approvedFile.toAbsolutePath(), e);
            throw new RuntimeException(message, e);
        }
    }

    public String readApproved() {
        Path approvedFile = approvedFile();

        try {
            return new String(Files.readAllBytes(approvedFile));
        } catch (IOException e) {
            String message = format("Could not read from file %s because of %s", approvedFile.toAbsolutePath(), e.getMessage());
//            throw new RuntimeException(message, e);
            return null;
        }
    }


    private Path folder() {
        String packageName = aClass.getPackage().getName();
        Path packageResourcesPath = Paths.get("src/test/resources/", packageName.split("\\."));
        return packageResourcesPath.resolve(aClass.getSimpleName());
    }

    public Path approvedFile() {
        return approvedFile(new StackUtils(aClass).methodName().get());
    }

    Path approvedFile(String methodName) {
        String fileName = format("%s.approved", methodName);
        return folder.resolve(fileName);
    }

    public void removeApproved(){
        try {
            delete(approvedFile());
        } catch (IOException e) {
            // we were cleaning just in case
        }
    }

}
