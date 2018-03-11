/*
 * Approvals-Java - Approval testing library for Java. Alleviates the burden of hand-writing assertions.
 * Copyright © 2018 Write Them First!
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.writethemfirst.approvals.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;

import static java.lang.String.format;
import static java.nio.file.Files.*;

/**
 * # FileUtils
 *
 * Set of methods to help and ease the manipulation of Files. Most likely adding minor behavior on top of existing Files
 * methods.
 *
 * Methods in that file are static so no instance of `FileUtils` is necessary.
 *
 * @author mdaviot / aneveux
 * @version 1.0
 */
public class FileUtils {

    /**
     * Reads the file located at the specified Path, and returns its content in case the file exists.
     *
     * If it doesn't exist or if it cannot be read, that method will return an empty String and ignore the {@link
     * IOException}. An error message will be written in {@link System#err} though.
     *
     * @param file The path of the file to be read
     * @return The content of the specified file if it exists and can be read, or an empty String otherwise
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
     * Removes recursively all elements if `file` is a directory, then removes `file`.
     *
     * If the file doesn't exist, nothing will be done.
     *
     * @param file the file to be removed
     */
    public static void silentRecursiveRemove(final File file) {
        if (file.isDirectory()) {
            final File[] children = file.listFiles();
            if (children != null) {
                Arrays.stream(children).forEach(FileUtils::silentRecursiveRemove);
            }
        }
        //noinspection ResultOfMethodCallIgnored
        file.delete();
    }

    /**
     * Writes the specified `content` in the `file` located at the specified Path.
     *
     * If the directories supposed to contain the specified `file` don't exist, they'll be created by this method
     * automatically. A message will be written in `System.err` and a `RuntimeException` will be thrown in case of any
     * error.
     *
     * The provided `content` will then be written in the provided `file`. A `RuntimeException` will be thrown in case
     * anything is going wrong will writing the content in the `file`.
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

    /**
     * Copies the content of a file found at a specified Path to another file located at another specified Path.
     *
     * It'll swallow all errors while reading the source file and only produce exceptions in case of errors while
     * writting the new file.
     *
     * @param source      The path from which the source file should be read (data to be copied)
     * @param destination The path to which the destination file should be written (containing the read data)
     */
    public static void copy(final Path source, final Path destination) {
        write(silentRead(source), destination);
    }
}
