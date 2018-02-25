package com.github.writethemfirst.approvals.utils;

import static java.lang.String.format;
import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.delete;
import static java.nio.file.Files.newBufferedWriter;

import java.io.BufferedWriter;
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


    public static void write(String content, Path file) {
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
