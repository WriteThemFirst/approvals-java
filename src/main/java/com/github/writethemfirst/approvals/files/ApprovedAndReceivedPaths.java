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

public class ApprovedAndReceivedPaths {
    public final Path approved;
    public final Path received;


    public ApprovedAndReceivedPaths(final Path approved, final Path received) {
        this.approved = approved;
        this.received = received;
    }

    public boolean filesHaveSameContent() {
        final String receivedContent = silentRead(received);
        final String approvedContent = silentRead(approved);
        return receivedContent.equals(approvedContent);
    }

    /**
     * When `this` represents a pairs of folders, returns a pair of files in these folders.
     *
     * @param approvedFile a file in the `approved` folder
     */
    public ApprovedAndReceivedPaths forApprovedFile(final Path approvedFile) {
        final Path approvedRelative = approved.relativize(approvedFile);
        final Path receivedFile = received.resolve(approvedRelative);
        return new ApprovedAndReceivedPaths(approvedFile, receivedFile);
    }

    /**
     * When `this` represents a pairs of folders, returns a pair of files in these folders.
     *
     * @param receivedFile a file in the `received` folder
     */
    public ApprovedAndReceivedPaths forReceivedFile(final Path receivedFile) {
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
}
