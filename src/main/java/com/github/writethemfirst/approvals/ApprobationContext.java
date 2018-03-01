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
package com.github.writethemfirst.approvals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.function.BiPredicate;

import static com.github.writethemfirst.approvals.utils.FileUtils.silentRead;
import static com.github.writethemfirst.approvals.utils.FileUtils.silentRemove;
import static com.github.writethemfirst.approvals.utils.FileUtils.write;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

/**
 * Wrapper around the *approved* and *received* files.
 *
 * The files are specified by a parent folder and a method name.
 */
public class ApprobationContext {
    private final Path folder;
    private final String methodName;

    public ApprobationContext(Path folder, String methodName) {
        this.folder = folder;
        this.methodName = methodName;
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

    /**
     * Writes content to a file relative to the *approved* folder.
     */
    public void writeApproved(final String content, Path relativeFile) {
        final Path approvedFile = approvedFile(relativeFile);
        write(content, approvedFile);
    }

    /**
     * Reads from a file relative to the *approved* folder.
     */
    public String readApproved(Path relativeFile) {
        return silentRead(approvedFile(relativeFile));
    }

    public void removeApproved(Path relativeFile) {
        silentRemove(approvedFile(relativeFile));
    }

    public void removeReceived(Path relativeFile) {
        silentRemove(receivedFile(relativeFile));
    }

    public Path approvedFile(Path relativeFile) {
        return approvedFolder().resolve(relativeFile + ".approved");
    }

    public Path receivedFile(Path relativeFile) {
        return approvedFolder().resolve(relativeFile + ".received");
    }

    /**
     * Reads the content of the *approved* file linked to the current method execution.
     *
     * That method will actually retrieve the method from which the call has been made and read the *approved* file
     * linked to the `testClass` attribute and the parent method calling this one.
     *
     * If the file doesn't exist, that method will simply display a message in `System.err` but won't fail.
     *
     * @return The content of the *approved* file linked to the parent method execution. An empty String if the file
     * doesn't exist.
     */
    public String readApproved() {
        return silentRead(approvedFile());
    }

    /**
     * Removes the *approved* file linked to the current method execution.
     *
     * That method will actually retrieve the method from which the call has been made and find the *approved* file
     * linked to the `testClass` attribute and the parent method calling this one.
     *
     * If the file doesn't exist, it won't do anything and won't return any kind of error.
     */
    public void removeApproved() {
        silentRemove(approvedFile());
    }

    /**
     * Writes the provided content in the *received* file linked to the current method execution.
     *
     * That method will actually retrieve the method from which the call has been made and name the *received* file from
     * the `testClass` attribute, but also the parent method calling this one.
     *
     * If the file doesn't exist, that method will create it in the `src/test/resources` folder.
     *
     * @param content Content to be written in the *received* file linked to the parent method execution.
     */
    public void writeReceived(final String content) {
        final Path receivedFile = receivedFile();
        write(content, receivedFile);
    }


    /**
     * Reads the content of the *received* file linked to the current method execution.
     *
     * That method will actually retrieve the method from which the call has been made and read the *received* file
     * linked to the `testClass` attribute and the parent method calling this one.
     *
     * If the file doesn't exist, that method will simply display a message in `System.err` but won't fail.
     *
     * @return The content of the *received* file linked to the parent method execution. An empty String if the file
     * doesn't exist.
     */
    public String readReceived() {
        return silentRead(receivedFile());
    }


    /**
     * Removes the *received* file linked to the current method execution.
     *
     * That method will actually retrieve the method from which the call has been made and find the *received* file
     * linked to the `testClass` attribute and the parent method calling this one.
     *
     * If the file doesn't exist, it won't do anything and won't return any kind of error.
     */
    public void removeReceived() {
        silentRemove(receivedFile());
    }

    public void createEmptyApprovedFileIfEmpty() {
        File approvedFile = approvedFile().toFile();
        if (!approvedFile.exists()) {
            try {
                approvedFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(format("Could not create empty approved file <%s>", approvedFile), e);
            }
        }
    }

    List<Path> approvedFilesInFolder() {
        int MAX_DEPTH = 5;
        BiPredicate<Path, BasicFileAttributes> followAllFiles = (path, attributes) -> attributes.isRegularFile();
        Path approvedFolder = approvedFolder();
        try {
            Files.createDirectories(approvedFolder);
            return Files
                .find(approvedFolder, MAX_DEPTH, followAllFiles)
                .collect(toList());
        } catch (IOException e) {
            throw new RuntimeException(format("cannot browse %s for approved files", approvedFolder), e);
        }
    }


    /**
     * Returns the *approved* file Path linked to the specified `methodName`.
     *
     * The Path will be computed by adding a `.approved` extension to the provided `methodName` and search for it in the
     * `folder` associated with the `testClass`.
     *
     * @return The Path to the *approved* file linked to the provided `methodName`.
     */
    Path approvedFile() {
        final String fileName = format("%s.approved", methodName);
        return folder.resolve(fileName);
    }

    /**
     * Returns the *received* file Path linked to the specified `methodName`.
     *
     * The Path will be computed by adding a `.received` extension to the provided `methodName` and search for it in the
     * `folder` associated with the `testClass`.
     *
     * @return The Path to the *received* file linked to the provided `methodName`.
     */
    Path receivedFile() {
        final String fileName = format("%s.received", methodName);
        return folder.resolve(fileName);
    }

    Path approvedFolder() {
        final String folderName = format("%s.Files", methodName);
        return folder.resolve(folderName);
    }
}
