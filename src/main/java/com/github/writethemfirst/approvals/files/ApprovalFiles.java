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
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.github.writethemfirst.approvals.utils.FileUtils.*;
import static java.lang.String.format;
import static java.util.stream.Collectors.partitioningBy;

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
     * Appends to the approved and received paths the name of a file.
     *
     * @param file the Path for which only the name is taken into account
     * @return an ApprovalsFile, on step deeper
     */
    public ApprovalFiles resolve(Path file) {
        final Path fileName = file.getFileName();
        return new ApprovalFiles(
            approved.resolve(fileName),
            received.resolve(fileName));
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
     * Compares the content of files in *approved* and *received* folders.
     *
     * @return the 2 lists of matches (files with same content) and mismatches (different files)
     */
    public MatchesAndMismatches matchesAndMismatches() {
        final Map<Boolean, List<ApprovalFiles>> matchesAndMismatches = listChildrenApprovalFiles()
            .collect(partitioningBy(ApprovalFiles::haveSameContent));
        return new MatchesAndMismatches(matchesAndMismatches.get(true), matchesAndMismatches.get(false));
    }


    /**
     * Creates a default approval file if it doesn't exist yet, with received content. If it already exists, that method
     * does nothing.
     */
    public void createApprovedFileIfNeeded() {
        final File file = approved.toFile();
        if (!file.exists()) {
            createParentDirectories(approved);
            FileUtils.copy(received, approved);
        }
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
        return folder.resolve(format("%s.%s", methodName.replaceAll(" ", "_"), extension));
    }

    /**
     * Checks if approved and received are directories and not regular *approved* and *received* files.
     *
     * @return true if both approved and received are directories
     */
    private boolean areRegularFiles() {
        return approved.toFile().isFile() && received.toFile().isFile();
    }

    /**
     * If approved and received are directories, this method allows to create a new {@link ApprovalFiles} object which
     * associates to an already known *approved* file a resolved *received* file. That *received* file will be searched
     * for in the received folder by resolving the relative path found from the *approved* file.
     *
     * It allows to go from a found *approved* file to a pair of both *approved* and *received* file.
     *
     * If approved or received are actualy not directories (which is checked with areRegularFiles), this current
     * instance of {@link ApprovalFiles} will be returned.
     *
     * @param approvedFile The *approved* file we already know and for which we want to associate a *received* file
     * @return An {@link ApprovalFiles} instance containing both the *approved* and matching *received* file
     */
    private ApprovalFiles associateMatchingReceivedFile(final Path approvedFile) {
        if (areRegularFiles())
            return this;
        final Path approvedRelativePath = approved.relativize(approvedFile);
        final Path receivedFile = received.resolve(approvedRelativePath);
        return new ApprovalFiles(approvedFile, receivedFile);
    }

    /**
     * If approved and received are directories, this method allows to create a new {@link ApprovalFiles} object which
     * associates to an already known *received* file a resolved *approved* file. That *approved* file will be searched
     * for in the approved folder by resolving the relative path found from the *received* file.
     *
     * It allows to go from a found *received* file to a pair of both *approved* and *received* file.
     *
     * If approved or received are actualy not directories (which is checked with areRegularFiles), this current
     * instance of {@link ApprovalFiles} will be returned.
     *
     * @param receivedFile The *received* file we already know and for which we want to associate a *approved* file
     * @return An {@link ApprovalFiles} instance containing both the *approved* and matching *received* file
     */
    private ApprovalFiles associateMatchingApprovedFile(final Path receivedFile) {
        if (areRegularFiles())
            return this;
        final Path receivedRelativePath = received.relativize(receivedFile);
        final Path approvedFile = approved.resolve(receivedRelativePath);
        return new ApprovalFiles(approvedFile, receivedFile);
    }

    /**
     * If approved and received are regular files, this method will check if both files have the same content (by
     * reading them and comparing the data afterwards).
     *
     * If approved and received actually are folders, this method will simply return false.
     *
     * @return True if approved and received are regular files and have the same content
     */
    public boolean haveSameContent() {
        if (!areRegularFiles())
            return false;
        final String receivedContent = silentRead(received);
        final String approvedContent = silentRead(approved);
        return receivedContent.equals(approvedContent);
    }

    /**
     * If approved and received actually are folders (which can be the case while using the folder approval features),
     * this method allows to list all the children approval files from those folders.
     *
     * It'll basically read all the files from the approved folder, all the files from the received folder, associate
     * the matching approved and received files for each of those, and of course filter the duplicates. This gives us a
     * complete list of all approval files in the end.
     *
     * In case that function is called when approved or received are not folders, it'll simply return an empty stream.
     *
     * @return A Stream containing all the pairs of approval files for the approved and received folders.
     */
    public Stream<ApprovalFiles> listChildrenApprovalFiles() {
        if (areRegularFiles())
            return Stream.empty();
        return Stream.concat(
            listFiles(approved).map(this::associateMatchingReceivedFile),
            listFiles(received).map(this::associateMatchingApprovedFile)
        ).distinct();
    }

    /**
     * **Overriding equals to allow filtering of duplicates.**
     *
     * @param o The object we want to compare to the current instance
     * @return true if both objects are considered equals
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final ApprovalFiles that = (ApprovalFiles) o;

        return approved.equals(that.approved) && received.equals(that.received);
    }

    /**
     * **Overriding hashCode to allow filtering of duplicates.**
     *
     * @return A unique hashcode for the particular data holded in that object.
     */
    @Override
    public int hashCode() {
        int result = approved.hashCode();
        result = 31 * result + received.hashCode();
        return result;
    }
}
