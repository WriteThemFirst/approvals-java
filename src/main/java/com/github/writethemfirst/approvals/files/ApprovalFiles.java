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

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static com.github.writethemfirst.approvals.utils.FileUtils.listFiles;
import static com.github.writethemfirst.approvals.utils.FileUtils.silentRead;

/**
 * # ApprovalFiles
 *
 * *Approval Testing* relies on 2 specific files for storing the execution results of the *Program Under Tests* and
 * running comparisons over the output of the program.
 *
 * *Approvals-Java* provides various features, including being able to approve single files, but also to validate the
 * output of a program using an approved folder (which can then contain various subfolders and files).
 *
 * This class allows to store references of the 2 particular entries to consider: the *approved* and *received* entries,
 * which are used to store the reference data, and the temporary output of the program for comparison. The class manages
 * both cases provided by the framework: single files, or complete folders.
 *
 * This class not only will hold the references of the *approved* and *received* entries, but it'll also provide all the
 * necessary methods allowing to compare and validate the files or folders.
 */
public class ApprovalFiles {

    /**
     * Path to an *approved* entry.
     *
     * This entry can contain:
     *
     * - A single *approved* file in case of a simple approval, - An *approved* folder buildApprovalFilePath in case of
     * a folder approval, - A reference to a simple file located in an *approved* folder, to be used for later
     * comparison
     */
    public final Path approved;

    /**
     * Path to an *received* entry.
     *
     * This entry can contain:
     *
     * - A single *received* file in case of a simple approval, - An *received* folder buildApprovalFilePath in case of
     * a folder approval, - A reference to a simple file located in an *received* folder, to be used for later
     * comparison
     */
    public final Path received;

    /**
     * Constructs an ApprovalFiles instance for a pair of *approved* and *received* entries.
     *
     * @param approved An *approved* entry (can be either a file or a folder)
     * @param received A *received* entry (can be either a file or a folder)
     */
    private ApprovalFiles(final Path approved, final Path received) {
        this.approved = approved;
        this.received = received;
    }

    /**
     * Builds a pair of approval entries from the provided folder, method name, and extension. The path for both
     * *approved* and *received* files will be computed and used as approval files.
     *
     * @param folder     The folder in which the approval files should be located
     * @param methodName The name of the method calling the test. It is used to actually name the approval files
     * @param extension  The extension to use for the approval file (by default could be either *approved* or
     *                   *received*)
     * @return An {@link ApprovalFiles} object, containing the pair of generated *approved* and *received* entries
     */
    //FIXME: That extension field should be removed or refactored. The String concatenation for the extension is super ugly.
    public static ApprovalFiles build(final Path folder, final String methodName, final String extension) {
        return new ApprovalFiles(
            buildApprovalFilePath(folder, methodName, "approved" + extension),
            buildApprovalFilePath(folder, methodName, "received" + extension));
    }

    /**
     * Builds and retrieves the path to an approval file from the folder, method name, and extension to use.
     *
     * @param folder     The folder in which the approval file will be searched for
     * @param methodName The name of the method calling the test. It is used to actually name the approval files
     * @param extension  The extension to use for the approval file (by default could be either *approved* or
     *                   *received*)
     * @return The path to the approval file computed from all the specified information
     */
    private static Path buildApprovalFilePath(final Path folder, final String methodName, final String extension) {
        return Paths.get(folder.resolve(methodName) + "." + extension);
    }

    public boolean filesHaveSameContent() {
        final String receivedContent = silentRead(received);
        final String approvedContent = silentRead(approved);
        return receivedContent.equals(approvedContent);
    }


    /**
     * When `this` is a pair of folders, constructs all pairs of files.
     */
    public Stream<ApprovalFiles> allFilesToCheck() {
        return Stream
            .concat(
                listFiles(approved).map(this::forApprovedFile),
                listFiles(received).map(this::forReceivedFile)
            )
            .distinct();
    }

    /**
     * When `this` represents a pairs of folders, returns a pair of files in these folders.
     *
     * @param approvedFile a file in the `approved` folder
     */
    private ApprovalFiles forApprovedFile(final Path approvedFile) {
        final Path approvedRelative = approved.relativize(approvedFile);
        final Path receivedFile = received.resolve(approvedRelative);
        return new ApprovalFiles(approvedFile, receivedFile);
    }

    /**
     * When `this` represents a pairs of folders, returns a pair of files in these folders.
     *
     * @param receivedFile a file in the `received` folder
     */
    private ApprovalFiles forReceivedFile(final Path receivedFile) {
        final Path receivedRelative = received.relativize(receivedFile);
        final Path approvedFile = approved.resolve(receivedRelative);
        return new ApprovalFiles(approvedFile, receivedFile);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final ApprovalFiles that = (ApprovalFiles) o;

        return approved.equals(that.approved) && received.equals(that.received);
    }

    @Override
    public int hashCode() {
        int result = approved.hashCode();
        result = 31 * result + received.hashCode();
        return result;
    }
}
