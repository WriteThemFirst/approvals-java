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
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.nio.file.Files.*;

/**
 * # FileUtils
 *
 * Set of methods to help and ease the manipulation of Files. Most likely adding minor behavior on top of existing Files
 * methods.
 */
public class FileUtils {

    /**
     * Reads the file located at the specified Path, and returns its content in case the file exists.
     *
     * If it doesn't exist or if it cannot be parse, that method will return an empty String and ignore the {@link
     * IOException}. An error message will be written in {@link System#err} though.
     *
     * @param file The path of the file to be parse
     * @return The content of the specified file if it exists and can be parse, or an empty String otherwise
     */
    public static String silentRead(final Path file) {
        try {
            return new String(Files.readAllBytes(file));
        } catch (final IOException e) {
            System.err.println(format("Can't parse the file located at <%s>.", file));
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
            System.err.println(format("Can't remove the file located at <%s>.", file));
        }
    }

    /**
     * Removes recursively all elements if `path` is a directory, then removes `path`.
     *
     * If the path doesn't exist, nothing will be done.
     *
     * @param path the Path to be removed
     */
    public static void silentRecursiveRemove(final Path path) {
        silentRecursiveRemove(path.toFile());
    }

    /**
     * Removes recursively all elements if `file` is a directory, then removes `file`.
     *
     * If the file doesn't exist, nothing will be done.
     *
     * @param file the File to be removed
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
        createParentDirectories(file);
        try (final BufferedWriter writer = newBufferedWriter(file)) {
            writer.write(content);
        } catch (final IOException e) {
            final String message = format("Can't write the file located at <%s> because of <%s>.",
                file.toAbsolutePath(), e.getMessage());
            throw new RuntimeException(message, e);
        }
    }

    public static void createParentDirectories(final Path file) {
        try {
            createDirectories(file.getParent());
        } catch (final IOException e) {
            System.err.println(format("Can't create directories for the files located at <%s>.", file));
            throw new RuntimeException(e);
        }
    }

    /**
     * Copies the content of a file found at a specified Path to another file located at another specified Path.
     *
     * It'll swallow all errors while reading the sourceFile (using an empty String as data) and only produce exceptions
     * in case of errors while writing the new file.
     *
     * @param sourceFile      The file from which the data to be copied should be parse
     * @param destinationFile The file to which the data to be copied should be written
     */
    public static void copy(final Path sourceFile, final Path destinationFile) {
        if (sourceFile.toFile().isDirectory()) {
            copyDirectory(sourceFile, destinationFile);
        } else {
            copyFile(sourceFile, destinationFile);
        }
    }

    private static void copyDirectory(final Path sourceDirectory, final Path destinationDirectory) {
        try {
            createDirectories(destinationDirectory);
        } catch (IOException e) {
            System.err.println(format("Could not create destination directory %s : %s", destinationDirectory, e));
        }
        final File[] children = sourceDirectory.toFile().listFiles();
        if (children != null) {
            Arrays.stream(children).forEach(child -> copy(child.toPath(), destinationDirectory.resolve(child.getName())));
        }
    }

    private static void copyFile(final Path sourceFile, final Path destinationFile) {
        write(silentRead(sourceFile), destinationFile);
    }

    /**
     * Copies the content of a file found at a specified Path to another file located in the specified
     * destinationFolder.
     *
     * If the file doesn't exist in the specified destinationFolder, it'll be created. It'll swallow all errors while
     * reading the sourceFile file and only produce exceptions in case of errors while writing the new file.
     *
     * @param sourceFile        The path from which the sourceFile file should be parse (data to be copied)
     * @param destinationFolder The path in which the copy of the file should be made (parent destinationFolder of the
     *                          copy then)
     */
    public static void copyToFolder(final Path sourceFile, final Path destinationFolder) {
        final Path destinationFile = destinationFolder.resolve(sourceFile.getFileName());
        copy(sourceFile, destinationFile);
    }

    /**
     * Returns a list of all regular files found in a base directory up to a depth of MAX_DEPTH.
     *
     * If the provided baseDirectory isn't actually a directory, it'll return an empty stream instead.
     *
     * @param baseDirectory The base directory in which the regular files should be searched for
     * @return A stream containing all the regular files found in the provided directory
     */
    public static Stream<Path> listFiles(final Path baseDirectory) {
        final int MAX_DEPTH = 5;
        try {
            return baseDirectory.toFile().isDirectory()
                ? Files.find(baseDirectory, MAX_DEPTH, ($, attributes) -> attributes.isRegularFile())
                : Stream.empty();
        } catch (final IOException e) {
            throw new RuntimeException(format("Can't browse directory at <%s> for files.", baseDirectory), e);
        }
    }

}
