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
package com.github.writethemfirst.approvals.files;

import com.github.writethemfirst.approvals.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.function.BiPredicate;

import static com.github.writethemfirst.approvals.utils.FileUtils.silentRead;
import static com.github.writethemfirst.approvals.utils.FileUtils.silentRemove;
import static java.lang.String.format;
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
 * That `ApprovalsFiles` class can be seen as a wrapper of the *approved* and *received* files for a particular {@link
 * ApprobationContext} since they allow to manage both files for a particular class and method. That wrapper class will
 * provide all the necessary methods to manage the approbation files.
 *
 * `ApprovalsFiles` won't automatically name the files but will only rely on the folder and methodName arguments it
 * received while being created.
 *
 * @author mdaviot / aneveux
 * @version 1.1
 */
public class ApprovalsFiles {

    /**
     * The folder in which the *approved* and *received* files are supposed to be created.
     *
     * It'll usually match with the test class name.
     */
    private final Path folder;

    /**
     * The caller test method name which will be used for naming the *approved* and *received* files.
     */
    private final String methodName;

    /**
     * Instance of the {@link ApprovedFile} linked to that {@link ApprovalsFiles} instance.
     */
    final public ApprovedFile approved;

    /**
     * Instance of the {@link ReceivedFile} linked to that {@link ApprovalsFiles} instance.
     */
    final public ReceivedFile received;

    /**
     * Constructs an {@link ApprovalsFiles} instance linked to the specified folder and methodName.
     *
     * @param folder     The folder in which the *approved* and *received* files are supposed to be created.
     * @param methodName The caller test method name which will be used for naming the *approved* and *received* files.
     */
    public ApprovalsFiles(final Path folder, final String methodName) {
        this.folder = folder;
        this.methodName = methodName;
        approved = new ApprovedFile();
        received = new ReceivedFile();
    }

    /**
     * Returns the path to a dedicated folder for the current {@link ApprobationContext}.
     *
     * That folder may be used for storing multiple files which will later be compared for approval.
     *
     * The folder name will be the method name followed by `.Files`.
     *
     * @return The path to a folder dedicated to storing the approvals files of the current context.
     */
    public Path approvalsFolder() {
        final String folderName = format("%s.Files", methodName);
        return folder.resolve(folderName);
    }

    /**
     * Returns a list of all the *approved* files contained in the current approvals folder. The *approved* files will
     * be identified by their file extension and will be searched to a maximum depth of 5 folders.
     *
     * @return A list of all *approved* files contained in the current approvals folder.
     */
    public List<Path> approvedFilesInFolder() {
        final int MAX_DEPTH = 5;
        final BiPredicate<Path, BasicFileAttributes> isAnApprovedFile = (path, attributes) ->
            attributes.isRegularFile() && path.toString().endsWith("." + ApprovedFile.APPROVED_EXTENSION);
        final Path approvalsFolder = approvalsFolder();
        try {
            Files.createDirectories(approvalsFolder);
            return Files
                .find(approvalsFolder, MAX_DEPTH, isAnApprovedFile)
                .collect(toList());
        } catch (final IOException e) {
            throw new RuntimeException(format("cannot browse %s for approved files", approvalsFolder), e);
        }
    }

    /**
     * # ApprovedFile
     *
     * In *Approval Testing*, the *approved* file is the one containing the reference data to be validated while
     * computing the tests. It is generated in a first place by the *Program Under Tests* and reviewed by the developer.
     * Once approved, it must be committed along with the source code of the project since it actually contains all the
     * data to be used for the tests assertions.
     *
     * This class aims at providing all the necessary methods for manipulating a particular *approved* file: from
     * reading to writing it.
     *
     * @author aneveux
     * @version 1.0
     * @since 1.1
     */
    public class ApprovedFile {

        /**
         * Extension to be used for all approved files.
         */
        public static final String APPROVED_EXTENSION = "approved";

        /**
         * Returns the *approved* file Path linked to the specified `methodName`.
         *
         * The Path will be computed by adding the APPROVED_EXTENSION to the provided `methodName` and search for it in
         * the `folder` associated with the `testClass`.
         *
         * @return The Path to the *approved* file linked to the provided `methodName`.
         */
        public Path get() {
            final String fileName = format("%s.%s", methodName, APPROVED_EXTENSION);
            return folder.resolve(fileName);
        }

        /**
         * Returns the Path of the *approved* file computed from the current approvalsFolder and the provided
         * relativeFile path. Which means it'll resolve automatically the approvals folder path, and then simply resolve
         * the relativeFile path within it.
         *
         * @param relativeFile The path of the *approved* file to search for in the current approvals folder
         * @return The complete path of the *approved* file found in the current approvals folder
         */
        public Path get(final Path relativeFile) {
            return approvalsFolder().resolve(format("%s.%s", relativeFile, APPROVED_EXTENSION));
        }

        /**
         * Reads the content of the *approved* file linked to the current {@link ApprobationContext}.
         *
         * That method will actually read the file which has been determined by the approbation context and will return
         * its content.
         *
         * If the file doesn't exist, that method will simply display a message in `System.err` but won't fail.
         *
         * @return The content of the *approved* file linked to the current {@link ApprobationContext}. An empty String
         * if the file doesn't exist.
         */
        public String read() {
            return silentRead(get());
        }

        /**
         * Reads the content of the custom *approved* file linked to the current {@link ApprobationContext}.
         *
         * The custom *approved* file will be retrieved from the approvalsFolder by searching for the provided
         * relativeFile in it. Once found, its content will be returned.
         *
         * If the file doesn't exist, that method will simply display a message in `System.err` but won't fail.
         *
         * @return The content of the *approved* file linked to the current {@link ApprobationContext}. An empty String
         * if the file doesn't exist.
         */
        public String read(final Path relativeFile) {
            return silentRead(get(relativeFile));
        }

        /**
         * Writes the provided content in the *approved* file linked to the current {@link ApprobationContext}.
         *
         * If the file doesn't exist, that method will create it in the `src/test/resources` folder.
         *
         * @param content Content to be written in the *approved* file linked to the parent method execution.
         */
        public void write(final String content) {
            final Path approvedFile = get();
            FileUtils.write(content, approvedFile);
        }

        /**
         * Writes the provided content in the custom *approved* file linked to the current {@link ApprobationContext}.
         *
         * The custom *approved* file will be retrieved from the approvalsFolder by searching for the provided
         * relativeFile in it. Once found, the specified content will be written in it.
         *
         * If the file doesn't exist, that method will create it in the `src/test/resources` folder.
         *
         * @param content Content to be written in the *approved* file linked to the parent method execution.
         */
        public void write(final String content, final Path relativeFile) {
            final Path approvedFile = get(relativeFile);
            FileUtils.write(content, approvedFile);
        }

        /**
         * Removes the *approved* file linked to the current {@link ApprobationContext}.
         *
         * If the file doesn't exist, it won't do anything and won't return any kind of error.
         */
        public void remove() {
            silentRemove(get());
        }

        /**
         * Removes the custom *approved* file linked to the current {@link ApprobationContext}.
         *
         * The custom *approved* file will be retrieved from the approvalsFolder by searching for the provided
         * relativeFile in it. Once found, it'll be removed.
         *
         * If the file doesn't exist, it won't do anything and won't return any kind of error.
         */
        public void remove(final Path relativeFile) {
            silentRemove(get(relativeFile));
        }

        /**
         * Creates an empty *approved* file if it doesn't exist yet.
         *
         * If it already exist, that method will do nothing. If there's any issue while creating the *approved* file,
         * the {@link IOException} will be wrapped in a {@link RuntimeException} and thrown.
         */
        public void init() {
            final File approvedFile = get().toFile();
            if (!approvedFile.exists()) {
                try {
                    //noinspection ResultOfMethodCallIgnored
                    approvedFile.createNewFile();
                } catch (final IOException e) {
                    throw new RuntimeException(format("Could not create empty approved file <%s>", approvedFile), e);
                }
            }
        }
    }

    /**
     * # ReceivedFile
     *
     * In *Approval Testing*, the *received* file is a temporary file which is generated from the output of the *Program
     * Under Test* and used for comparing the results with the reference of the *approved* file. That file will then be
     * deleted to avoid polluting the codebase. In case of the tests failing, the file will most likely be kept for
     * further review by the developer.
     *
     * This class aims at providing all the necessary methods for manipulating a particular *received* file: from
     * reading to writing it.
     *
     * @author aneveux
     * @version 1.0
     * @since 1.1
     */
    public class ReceivedFile {

        /**
         * Extension to be used for all received files.
         */
        public static final String RECEIVED_EXTENSION = "received";

        /**
         * Returns the *received* file Path linked to the specified `methodName`.
         *
         * The Path will be computed by adding the RECEIVED_EXTENSION to the provided `methodName` and search for it in
         * the `folder` associated with the `testClass`.
         *
         * @return The Path to the *received* file linked to the provided `methodName`.
         */
        public Path get() {
            final String fileName = format("%s.%s", methodName, RECEIVED_EXTENSION);
            return folder.resolve(fileName);
        }

        /**
         * Returns the Path of the *received* file computed from the current approvalsFolder and the provided
         * relativeFile path. Which means it'll resolve automatically the approvals folder path, and then simply resolve
         * the relativeFile path within it.
         *
         * @param relativeFile The path of the *received* file to search for in the current approvals folder
         * @return The complete path of the *received* file found in the current approvals folder
         */
        public Path get(final Path relativeFile) {
            return approvalsFolder().resolve(format("%s.%s", relativeFile, RECEIVED_EXTENSION));
        }

        /**
         * Reads the content of the *received* file linked to the current {@link ApprobationContext}.
         *
         * That method will actually read the file which has been determined by the approbation context and will return
         * its content.
         *
         * If the file doesn't exist, that method will simply display a message in `System.err` but won't fail.
         *
         * @return The content of the *received* file linked to the current {@link ApprobationContext}. An empty String
         * if the file doesn't exist.
         */
        public String read() {
            return silentRead(get());
        }

        /**
         * Writes the provided content in the *received* file linked to the current {@link ApprobationContext}.
         *
         * If the file doesn't exist, that method will create it in the `src/test/resources` folder.
         *
         * @param content Content to be written in the *received* file linked to the parent method execution.
         */
        public void write(final String content) {
            final Path receivedFile = get();
            FileUtils.write(content, receivedFile);
        }

        /**
         * Writes the provided content in the custom *received* file linked to the current {@link ApprobationContext}.
         *
         * The custom *received* file will be retrieved from the approvalsFolder by searching for the provided
         * relativeFile in it. Once found, the specified content will be written in it.
         *
         * If the file doesn't exist, that method will create it in the `src/test/resources` folder.
         *
         * @param content Content to be written in the *received* file linked to the parent method execution.
         */
        public void write(final String content, final Path relativeFile) {
            final Path receivedFile = get(relativeFile);
            FileUtils.write(content, receivedFile);
        }

        /**
         * Removes the *received* file linked to the current {@link ApprobationContext}.
         *
         * If the file doesn't exist, it won't do anything and won't return any kind of error.
         */
        public void remove() {
            silentRemove(get());
        }

        /**
         * Removes the custom *received* file linked to the current {@link ApprobationContext}.
         *
         * The custom *received* file will be retrieved from the approvalsFolder by searching for the provided
         * relativeFile in it. Once found, it'll be removed.
         *
         * If the file doesn't exist, it won't do anything and won't return any kind of error.
         */
        public void remove(final Path relativeFile) {
            silentRemove(get(relativeFile));
        }
    }

}
