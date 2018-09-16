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

import static com.github.writethemfirst.approvals.utils.FileUtils.silentRead;
import static java.lang.String.format;

public class ApprovedAndReceived {
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
    public ApprovedAndReceived(final Path folder, final String methodName) {
        this(
            approvalFilePath(folder, methodName, "approved"),
            approvalFilePath(folder, methodName, "received"));
    }

    public ApprovedAndReceived(final Path approved, final Path received) {
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
        if (!(o instanceof ApprovedAndReceived)) return false;

        final ApprovedAndReceived that = (ApprovedAndReceived) o;

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
