package com.github.writethemfirst.approvals.utils;

import static java.nio.file.Files.delete;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtils {
    public static String silentRead(Path file) {
        try {
            return new String(Files.readAllBytes(file));
        } catch (IOException e) {
            System.err.println("Could not read from " + file);
            return "";
        }
    }


    public static void silentRemove(Path path) {
        try {
            delete(path);
        } catch (IOException e) {
            // we were cleaning just in case
        }
    }
}
