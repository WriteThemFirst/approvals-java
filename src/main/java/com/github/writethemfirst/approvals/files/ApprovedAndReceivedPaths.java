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

    public  ApprovedAndReceivedPaths approvedAndReceived(final Path approvedFile) {
        final Path approvedRelative = approved.relativize(approvedFile);
        final Path receivedFile = received.resolve(approvedRelative);
        return new ApprovedAndReceivedPaths(approvedFile, receivedFile);
    }

}
