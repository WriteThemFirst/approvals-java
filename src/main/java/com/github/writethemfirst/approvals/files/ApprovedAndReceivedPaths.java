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
import java.util.List;
import java.util.stream.Collectors;

import static com.github.writethemfirst.approvals.utils.FileUtils.searchFiles;
import static com.github.writethemfirst.approvals.utils.FileUtils.silentRead;

public class ApprovedAndReceivedPaths {
    public final Path approvedFile;
    public final Path receivedFile;


    public ApprovedAndReceivedPaths(final Path approvedFile, final Path receivedFile) {
        this.approvedFile = approvedFile;
        this.receivedFile = receivedFile;
    }

    public boolean haveSameContent() {
        final String receivedContent = silentRead(receivedFile);
        final String approvedContent = silentRead(approvedFile);
        return receivedContent.equals(approvedContent);
    }
}
