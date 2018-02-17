package org.approvalsj.util;

import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {
    private final Class<?> aClass;
    private final Path folder;

    public FileUtils(Class<?> aClass) {
        this.aClass = aClass;
        this.folder = getFolder();
    }

    public Path getApprovedFile(String methodName) {
        String fileName = String.format("%s.approved", methodName);
        return folder.resolve(fileName);
    }

    private Path getFolder() {
        String packageName = aClass.getPackage().getName();
        Path packageResourcesPath = Paths.get("src/test/resources/", packageName.split("\\."));
        return packageResourcesPath.resolve(aClass.getSimpleName());
    }

    public Path getApprovedFile() {
        return getApprovedFile(new StackUtils(aClass).methodName().get());
    }
}
