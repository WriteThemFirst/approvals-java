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

import static com.github.writethemfirst.approvals.utils.FileUtils.*;
import static com.github.writethemfirst.approvals.utils.StackUtils.callerClass;
import static com.github.writethemfirst.approvals.utils.StackUtils.callerMethod;
import static java.lang.String.format;
import static java.nio.file.Paths.get;
import static java.util.stream.Collectors.toList;

/**
 * # ApprovalsFiles
 *
 * *Approval Testing* relies on comparing data produced by a *Program Under Tests* and data which has been reviewed and
 * validated by the developer. This validated data is stored in *approved* files along with the project, and is used for
 * comparisons with the data produced by the program's execution.
 *
 * Two files are needed for computing *Approval Tests*: an *approved* file, which should be committed with the source
 * code, containing data validated by the developer, and a *received* file, which temporarily holds the data produced by
 * the *Program Under Tests* execution. Those files are then compared to validate the proper behavior of the program.
 *
 * That `ApprovalsFiles` class can be seen as a companion to a test class since it provides useful methods allowing to
 * read and write both *approved* and *received* files. It is linked to a test class since the produced files will be
 * named after the test class and its methods.
 *
 * An `ApprovalsFiles` object will be automatically created and attached to any created `Approvals` object, but you may
 * choose to instantiate one yourself for accessing and managing the *approved* and *received* files of a particular
 * class.
 *
 * @author mdaviot / aneveux
 * @version 1.0
 */
public class ApprovalsFiles {

    private final Class<?> testClass;
    private final Path folder;

    /**
     * Constructs an `ApprovalsFiles` using the {@link com.github.writethemfirst.approvals.utils.StackUtils#callerClass(Class)}.
     */
    public ApprovalsFiles() {
        this(callerClass(ApprovalsFiles.class));
    }

    /**
     * Constructs an `ApprovalsFiles`.
     *
     * @param testClass The test class linked to this instance. Created files will contain that class name in their
     *                  path.
     */
    ApprovalsFiles(final Class<?> testClass) {
        this.testClass = testClass;
        this.folder = folderForClass();
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


    /**
     * Computes and returns the *approved* file Path linked to the current method execution.
     *
     * That method will actually retrieve the method from which the call has been made and locate the *approved* file
     * linked to the `testClass` attribute and the parent method calling this one.
     *
     * If the parent method cannot be found in the stack hierarchy, the method name will be replaced by `unknown_method`
     * so it can easily be found in the files.
     *
     * @return The Path to the *approved* file linked to the current method execution, or to a specific file in case
     * that calling hierarchy cannot be established.
     */
    public Path approvedFile() {
        return approvedFile(callerMethodName());
    }


    /**
     * Computes and returns the *received* file Path linked to the current method execution.
     *
     * That method will actually retrieve the method from which the call has been made and locate the *received* file
     * linked to the `testClass` attribute and the parent method calling this one.
     *
     * If the parent method cannot be found in the stack hierarchy, the method name will be replaced by `unknown_method`
     * so it can easily be found in the files.
     *
     * @return The Path to the *received* file linked to the current method execution, or to a specific file in case
     * that calling hierarchy cannot be established.
     */
    public Path receivedFile() {
        return receivedFile(callerMethodName());
    }

    public Path approvedFolder() {
        return approvedFolder(callerMethodName());
    }

    public List<Path> approvedFilesInFolder() {
        return approvedFilesInFolder(callerMethodName());
    }

    Path approvedFolder(String methodName) {
        final String folderName = format("%s.Files", methodName);
        return folder.resolve(folderName);
    }

    List<Path> approvedFilesInFolder(String methodName) {
        int MAX_DEPTH = 5;
        BiPredicate<Path, BasicFileAttributes> followAllFiles = (path, attributes) -> attributes.isRegularFile();
        Path approvedFolder = approvedFolder(methodName);
        try {
            return Files
                .find(approvedFolder, MAX_DEPTH, followAllFiles)
                .collect(toList());
        } catch (IOException e) {
            throw new RuntimeException(format("cannot browse %s for approved files", approvedFolder), e);
        }
    }

    /**
     * Returns the *received* file Path linked to the specified `methodName`.
     *
     * The Path will be computed by adding a `.received` extension to the provided `methodName` and search for it in the
     * `folder` associated with the `testClass`.
     *
     * @param methodName The methodName to be used as a basis for the *received* file name to be used.
     * @return The Path to the *received* file linked to the provided `methodName`.
     */
    Path receivedFile(final String methodName) {
        final String fileName = format("%s.received", methodName);
        return folder.resolve(fileName);
    }


    /**
     * Returns the *approved* file Path linked to the specified `methodName`.
     *
     * The Path will be computed by adding a `.approved` extension to the provided `methodName` and search for it in the
     * `folder` associated with the `testClass`.
     *
     * @param methodName The methodName to be used as a basis for the *approved* file name to be used.
     * @return The Path to the *approved* file linked to the provided `methodName`.
     */
    Path approvedFile(final String methodName) {
        final String fileName = format("%s.approved", methodName);
        return folder.resolve(fileName);
    }


    /**
     * Computes and returns the Path to the folder to be used for storing the *approved* and *received* files linked to
     * the `testClass` instance.
     *
     * The folder will be created under `src/test/resources` in the really same project, and will be named after the
     * package name of the `testClass`, followed by the name of the `testClass` itself. That folder will later contain
     * one pair of files (*approved* and *received*) for each method to be tested.
     *
     * @return The Path to the folder linked to the `testClass` attribute, used for storing the *received* and
     * *approved* files.
     */
    private Path folderForClass() {
        final String packageName = testClass.getPackage().getName();
        final Path packageResourcesPath = get("src/test/resources/", packageName.split("\\."));
        return packageResourcesPath.resolve(testClass.getSimpleName());
    }

    private String callerMethodName() {
        return callerMethod(testClass).orElse("unknown_method");
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
}
