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

import static com.github.writethemfirst.approvals.utils.FileUtils.searchFiles;
import static com.github.writethemfirst.approvals.utils.FileUtils.silentRead;

/**
 * Convenience class to store a pair of *approved* and *received* paths.
 *
 * These paths usually point to text files which can be compared with {@link #filesHaveSameContent()}.
 *
 * When used by {@link com.github.writethemfirst.approvals.Approvals#verifyAgainstMasterFolder(Path)}, they can also
 * point either to:
 *
 * - the pair of *approved* and *received* folders which are globally compared - a pair of files (with no special
 * extension this time) in these folders
 *
 * {@link #equals(Object)} and {@link #hashCode()} have been overridden so we can filter out duplicates.
 */
public class ApprovedAndReceivedPaths {
    public final Path approved;
    public final Path received;


    private ApprovedAndReceivedPaths(final Path approved, final Path received) {
        this.approved = approved;
        this.received = received;
    }

    public boolean filesHaveSameContent() {
        final String receivedContent = silentRead(received);
        final String approvedContent = silentRead(approved);
        return receivedContent.equals(approvedContent);
    }


    /**
     * When `this` is a pair of folders, constructs all pairs of files.
     */
    public Stream<ApprovedAndReceivedPaths> allFilesToCheck() {
        return Stream
            .concat(
                searchFiles(approved).map(this::forApprovedFile),
                searchFiles(received).map(this::forReceivedFile)
            )
            .distinct();
    }

    /**
     * When `this` represents a pairs of folders, returns a pair of files in these folders.
     *
     * @param approvedFile a file in the `approved` folder
     */
    private ApprovedAndReceivedPaths forApprovedFile(final Path approvedFile) {
        final Path approvedRelative = approved.relativize(approvedFile);
        final Path receivedFile = received.resolve(approvedRelative);
        return new ApprovedAndReceivedPaths(approvedFile, receivedFile);
    }

    /**
     * When `this` represents a pairs of folders, returns a pair of files in these folders.
     *
     * @param receivedFile a file in the `received` folder
     */
    private ApprovedAndReceivedPaths forReceivedFile(final Path receivedFile) {
        final Path receivedRelative = received.relativize(receivedFile);
        final Path approvedFile = approved.resolve(receivedRelative);
        return new ApprovedAndReceivedPaths(approvedFile, receivedFile);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final ApprovedAndReceivedPaths that = (ApprovedAndReceivedPaths) o;

        return approved.equals(that.approved) && received.equals(that.received);
    }

    @Override
    public int hashCode() {
        int result = approved.hashCode();
        result = 31 * result + received.hashCode();
        return result;
    }


    /**
     * Factory method for standard names.
     */
    public static ApprovedAndReceivedPaths approvedAndReceived(final Path folder, final String methodName) {
        return new ApprovedAndReceivedPaths(
            path(folder, methodName, "approved"),
            path(folder, methodName, "received"));
    }

    private static Path path(final Path folder, final String methodName, final String extension) {
        return Paths.get(folder.resolve(methodName) + "." + extension);
    }

}
