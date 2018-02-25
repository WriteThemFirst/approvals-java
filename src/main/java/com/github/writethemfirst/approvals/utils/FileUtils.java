package com.github.writethemfirst.approvals.utils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.lang.String.format;
import static java.nio.file.Files.*;

/**
 * # FileUtils
 *
 * Set of methods to help and ease the manipulation of Files. Most likely adding minor behavior on top of existing Files
 * methods.
 *
 * Methods in that file are to be static so no instance of `FileUtils` is necessary.
 *
 * @author mdaviot / aneveux
 * @version 1.0
 */
public class FileUtils {

    /**
     * Reads the file located at the specified Path, and returns its content in case the file exists.
     *
     * If it doesn't exist or if it cannot be read, that method will return an empty String and swallow the Exception.
     * An error message will be written in `System.err` though.
     *
     * @param file The path of the file to be read
     * @return The content of the specified file if it exists and can be read, or an empty String otherwise.
     */
    public static String silentRead(final Path file) {
        try {
            return new String(Files.readAllBytes(file));
        } catch (final IOException e) {
            System.err.println("Could not read from " + file);
            return "";
        }
    }


    /**
     * Removes the file located at the specified Path if it exists.
     *
     * If the file doesn't exist, nothing will be done.
     *
     * If the file exists but cannot be removed, an error will simply be written in `System.err` and the Exception won't
     * be propagated (since it doesn't block the framework from working properly). We strongly advise to have a look at
     * the problem though.
     *
     * @param file The path of the file to be removed
     */
    public static void silentRemove(final Path file) {
        try {
            deleteIfExists(file);
        } catch (final IOException e) {
            System.err.println("Warning: couldn't remove the file located at: " + file);
        }
    }


    /**
     * Writes the specified `content` in the `file` located at the specified Path.
     *
     * If the directories supposed to contain the specified `file` don't exist, they'll be created by this method
     * automatically. A message will be written in `System.err` and a `RuntimeException` will be thrown in case of any
     * error.
     *
     * The provided `content` will then be written in the provided `file`. A `RuntimeException` will be thrown in case
     * anything is going wrong will writting the content in the `file`.
     *
     * @param content The content to be written in the specified file
     * @param file    The file in which the content should be written
     */
    public static void write(final String content, final Path file) {
        try {
            createDirectories(file.getParent());
        } catch (final IOException e) {
            System.err.println("Error: cannot create directories for: " + file);
            throw new RuntimeException(e);
        }
        try (BufferedWriter writer = newBufferedWriter(file)) {
            writer.write(content);
        } catch (final IOException e) {
            final String message = format("Could not write to file %s because of %s", file.toAbsolutePath(), e);
            throw new RuntimeException(message, e);
        }
    }
}
