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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.function.BiPredicate;

import static com.github.writethemfirst.approvals.utils.FileUtils.*;
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
     * Constructs an {@link ApprovalsFiles} instance linked to the specified folder and methodName.
     *
     * @param folder     The folder in which the *approved* and *received* files are supposed to be created.
     * @param methodName The caller test method name which will be used for naming the *approved* and *received* files.
     */
    public ApprovalsFiles(final Path folder, final String methodName) {
        this.folder = folder;
        this.methodName = methodName;
    }

    public ApprovedFile approvedFile = new ApprovedFile();
    public ReceivedFile receivedFile = new ReceivedFile();

    public Path approvalsFolder() {
        final String folderName = format("%s.Files", methodName);
        return folder.resolve(folderName);
    }

    public class ApprovedFile {
        public Path approvedFile(Path relativeFile) {
            return approvalsFolder().resolve(relativeFile + ".approved");
        }

        /**
         * Writes the provided content in the *approved* file linked to the current method execution.
         *
         * That method will actually retrieve the method from which the call has been made and name the *approved* file
         * from the `testClass` attribute, but also the parent method calling this one.
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

        public List<Path> approvedFilesInFolder() {
            int MAX_DEPTH = 5;
            BiPredicate<Path, BasicFileAttributes> approvedExtension = (path, attributes) ->
                attributes.isRegularFile() && path.toString().endsWith(".approved");
            Path approvedFolder = approvalsFolder();
            try {
                Files.createDirectories(approvedFolder);
                return Files
                    .find(approvedFolder, MAX_DEPTH, approvedExtension)
                    .collect(toList());
            } catch (IOException e) {
                throw new RuntimeException(format("cannot browse %s for approved files", approvedFolder), e);
            }
        }

        /**
         * Returns the *approved* file Path linked to the specified `methodName`.
         *
         * The Path will be computed by adding a `.approved` extension to the provided `methodName` and search for it in
         * the `folder` associated with the `testClass`.
         *
         * @return The Path to the *approved* file linked to the provided `methodName`.
         */
        public Path approvedFile() {
            final String fileName = format("%s.approved", methodName);
            return folder.resolve(fileName);
        }
    }

    public class ReceivedFile {
        public void writeReceived(final String content, Path relativeFile) {
            final Path receivedFile = receivedFile(relativeFile);
            write(content, receivedFile);
        }


        public void removeReceived(Path relativeFile) {
            silentRemove(receivedFile(relativeFile));
        }


        public Path receivedFile(Path relativeFile) {
            return approvalsFolder().resolve(relativeFile + ".received");
        }


        /**
         * Writes the provided content in the *received* file linked to the current method execution.
         *
         * That method will actually retrieve the method from which the call has been made and name the *received* file
         * from the `testClass` attribute, but also the parent method calling this one.
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
         * Returns the *received* file Path linked to the specified `methodName`.
         *
         * The Path will be computed by adding a `.received` extension to the provided `methodName` and search for it in
         * the `folder` associated with the `testClass`.
         *
         * @return The Path to the *received* file linked to the provided `methodName`.
         */
        public Path receivedFile() {
            final String fileName = format("%s.received", methodName);
            return folder.resolve(fileName);
        }
    }

}
