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
import java.nio.file.Path;

import static com.github.writethemfirst.approvals.utils.FileUtils.createParentDirectories;
import static com.github.writethemfirst.approvals.utils.FileUtils.silentRead;
import static java.lang.String.format;

/**
 * # ApprovalFiles
 *
 * *Approval Testing* relies on 2 specific files for storing the execution results of the *Program Under Tests* and
 * running comparisons over the output of the program.
 *
 * *Approvals-Java* provides various features, including being able to approve single files, but also to validate the
 * output of a program using an approved folder (which can then contain various sub-folders and files).
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
     * - A single *approved* file in case of a simple approval, - An *approved* folder approvalFilePath in case of a
     * folder approval, - A reference to a simple file located in an *approved* folder, to be used for later comparison
     */
    public final Path approved;

    /**
     * Path to an *received* entry.
     *
     * This entry can contain:
     *
     * - A single *received* file in case of a simple approval, - An *received* folder approvalFilePath in case of a
     * folder approval, - A reference to a simple file located in an *received* folder, to be used for later comparison
     */
    public final Path received;

    /**
     * Constructs a pair of approval entries from the provided folder and method name. The path for both *approved* and
     * *received* files will be computed and used as approval files.
     *
     * @param folder     The folder in which the approval files will be located
     * @param methodName The name of the method calling the test. It is used to actually name the approval files
     */
    public ApprovalFiles(final Path folder, final String methodName) {
        this(
            approvalFilePath(folder, methodName, "approved"),
            approvalFilePath(folder, methodName, "received"));
    }

    public ApprovalFiles(final Path approved, final Path received) {
        this.approved = approved;
        this.received = received;
    }


    public String approvedContent() {
        return silentRead(approved);
    }

    public String receivedContent() {
        return silentRead(received);
    }

    /**
     * Checks if both files have the same content (by reading them and comparing the data afterwards).
     */
    public boolean haveSameContent() {
        return receivedContent().equals(approvedContent());
    }

    /**
     * Creates an empty approval file if it doesn't exist yet. If it already exists, that method does nothing.
     */
    public void createEmptyApprovedFileIfNeeded() {
        final File file = approved.toFile();
        if (!file.exists()) {
            try {
                createParentDirectories(approved);
                //noinspection ResultOfMethodCallIgnored
                file.createNewFile();
            } catch (final IOException e) {
                throw new RuntimeException(format("Can't create an empty file at <%s>.", file), e);
            }
        }
    }

    public ApprovalFolders parent() {
        return new ApprovalFolders(approved.getParent(), received.getParent());
    }


    /**
     * Builds the path to an approval file from the folder, method name, and extension to use.
     *
     * @param folder     The folder in which the approval file will be searched for
     * @param methodName The name of the method calling the test. It is used to actually name the approval files
     * @param extension  The extension to use for the approval file (by default could be either *approved* or
     *                   *received*)
     * @return The path to the approval file computed from all the specified information
     */
    private static Path approvalFilePath(final Path folder, final String methodName, final String extension) {
        return folder.resolve(format("%s.%s", methodName.replaceAll(" ", "_"), extension));
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
        if (!(o instanceof ApprovalFiles)) return false;

        final ApprovalFiles that = (ApprovalFiles) o;

        return approved.equals(that.approved) && received.equals(that.received);
    }

    /**
     * **Overriding hashCode to allow filtering of duplicates.**
     *
     * @return A unique hashcode for the particular data held in that object.
     */
    @Override
    public int hashCode() {
        int result = approved.hashCode();
        result = 31 * result + received.hashCode();
        return result;
    }
}
