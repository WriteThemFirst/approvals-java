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
 * @version 1.2
 */
public class ApprovalsFiles {

    /**
     * The folder in which the *approved* and *received* files are supposed to be created.
     *
     * It'll usually match with the test class name.
     */
    @SuppressWarnings("PackageVisibleField")
    final Path folder;

    /**
     * The caller test method name which will be used for naming the *approved* and *received* files.
     */
    @SuppressWarnings("PackageVisibleField")
    final String methodName;

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
        approved = new ApprovedFile(folder, methodName);
        received = new ReceivedFile(folder, methodName);
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
            attributes.isRegularFile() && path.toString().endsWith(".approved");
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
     * Checks if the *received* file's content matches with the *approved* file's content.
     *
     * @return true if *approved* and *received* file have the exact same content. False otherwise.
     */
    public boolean haveSameContent() {
        return received.match(approved);
    }

    /**
     * # ApprovedFile
     *
     * That class allows to manipulate *approved* files.
     *
     * @author aneveux
     * @version 1.0
     * @see ApprovalFile
     * @since 1.2
     */
    public class ApprovedFile extends ApprovalFile {
        /**
         * Constructs an approval file for a particular {@link ApprobationContext}
         *
         * @param folder     The folder in which the approval files are supposed to be created
         * @param methodName The method name to be used for naming the approval files
         */
        public ApprovedFile(final Path folder, final String methodName) {
            super(folder, methodName);
        }

        @Override
        public String extension() {
            return "approved";
        }
    }

    /**
     * # ReceivedFile
     *
     * That class allows to manipulate *received* files.
     *
     * @author aneveux
     * @version 1.0
     * @see ApprovalFile
     * @since 1.2
     */
    public class ReceivedFile extends ApprovalFile {
        /**
         * Constructs an approval file for a particular {@link ApprobationContext}
         *
         * @param folder     The folder in which the approval files are supposed to be created
         * @param methodName The method name to be used for naming the approval files
         */
        ReceivedFile(final Path folder, final String methodName) {
            super(folder, methodName);
        }

        @Override
        public String extension() {
            return "received";
        }
    }

    /**
     * # ApprovalFile
     *
     * Abstract class representing all the operations that can be done on approval files. It allows to manage in the
     * same way both *approved* and *received* files by reusing the same code.
     *
     * The only method to be overriden in that class is the one providing the extension of the file, which allows to
     * manage properly the *approved* and *received* files.
     *
     * @author mdaviot / aneveux
     * @version 1.0
     * @since 1.2
     */
    private abstract class ApprovalFile {

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
         * Returns the proper extension to be used for the current approval file.
         *
         * @return the extension (without the `.` before) of the current approval file.
         */
        public abstract String extension();

        /**
         * Constructs an approval file for a particular {@link ApprobationContext}
         *
         * @param folder     The folder in which the approval files are supposed to be created
         * @param methodName The method name to be used for naming the approval files
         */
        ApprovalFile(final Path folder, final String methodName) {
            this.folder = folder;
            this.methodName = methodName;
        }

        /**
         * Returns the approval file Path linked to the specified `methodName`.
         *
         * The Path will be computed by adding the extension to the provided `methodName` and search for it in the
         * `folder` associated with the `testClass`.
         *
         * @return The Path to the approval file linked to the provided `methodName`.
         */
        public Path get() {
            final String fileName = String.format("%s.%s", methodName, extension());
            return folder.resolve(fileName);
        }

        /**
         * Returns the Path of the approval file computed from the current approvalsFolder and the provided relativeFile
         * path. Which means it'll resolve automatically the approvals folder path, and then simply resolve the
         * relativeFile path within it.
         *
         * @param relativeFile The path of the approval file to search for in the current approvals folder
         * @return The complete path of the approval file found in the current approvals folder
         */
        public Path get(final Path relativeFile) {
            return approvalsFolder().resolve(format("%s.%s", relativeFile, extension()));
        }

        /**
         * Reads the content of the approval file linked to the current {@link ApprobationContext}.
         *
         * If the file doesn't exist, that method will simply display a message in `System.err` but won't fail.
         *
         * @return The content of the approval file linked to the current {@link ApprobationContext}. An empty String if
         * the file doesn't exist.
         */
        public String read() {
            return silentRead(get());
        }

        /**
         * Reads the content of the custom approval file linked to the current {@link ApprobationContext}.
         *
         * The custom approval file will be retrieved from the approvalsFolder by searching for the provided
         * relativeFile in it. Once found, its content will be returned.
         *
         * If the file doesn't exist, that method will simply display a message in `System.err` but won't fail.
         *
         * @return The content of the approval file linked to the current {@link ApprobationContext}. An empty String if
         * the file doesn't exist.
         */
        public String read(final Path relativeFile) {
            return silentRead(get(relativeFile));
        }

        /**
         * Writes the provided content in the approval file linked to the current {@link ApprobationContext}.
         *
         * If the file doesn't exist, that method will create it in the `src/test/resources` folder.
         *
         * @param content Content to be written in the approval file linked to the parent method execution.
         */
        public void write(final String content) {
            FileUtils.write(content, get());
        }

        /**
         * Writes the provided content in the custom approval file linked to the current {@link ApprobationContext}.
         *
         * The custom approval file will be retrieved from the approvalsFolder by searching for the provided
         * relativeFile in it. Once found, the specified content will be written in it.
         *
         * If the file doesn't exist, that method will create it in the `src/test/resources` folder.
         *
         * @param content Content to be written in the approval file linked to the parent method execution.
         */
        public void write(final String content, final Path relativeFile) {
            FileUtils.write(content, get(relativeFile));
        }

        /**
         * Removes the approval file linked to the current {@link ApprobationContext}.
         *
         * If the file doesn't exist, it won't do anything and won't return any kind of error.
         */
        public void remove() {
            silentRemove(get());
        }

        /**
         * Removes the custom approval file linked to the current {@link ApprobationContext}.
         *
         * The custom approval file will be retrieved from the approvalsFolder by searching for the provided
         * relativeFile in it. Once found, it'll be removed.
         *
         * If the file doesn't exist, it won't do anything and won't return any kind of error.
         */
        public void remove(final Path relativeFile) {
            silentRemove(get(relativeFile));
        }

        /**
         * Creates an empty approval file if it doesn't exist yet.
         *
         * If it already exist, that method will do nothing. If there's any issue while creating the approval file, the
         * {@link IOException} will be wrapped in a {@link RuntimeException} and thrown.
         */
        public void init() {
            final File file = get().toFile();
            if (!file.exists()) {
                try {
                    //noinspection ResultOfMethodCallIgnored
                    file.createNewFile();
                } catch (final IOException e) {
                    throw new RuntimeException(format("Could not create empty file <%s>", file), e);
                }
            }
        }

        /**
         * Reads and compares the current ApprovalFile and the other one provided as an argument.
         *
         * All errors while reading the files will be swallowed.
         *
         * @param other an approval file to be read and compared to the current one
         * @return true if both approval file contain the exact same content. False otherwise.
         */
        public boolean match(final ApprovalFile other) {
            final String content = silentRead(get());
            final String otherContent = silentRead(other.get());
            return content.equals(otherContent);
        }
    }

}
