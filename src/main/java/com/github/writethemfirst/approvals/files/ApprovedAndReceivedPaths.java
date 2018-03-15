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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.function.BiPredicate;

import static com.github.writethemfirst.approvals.utils.FileUtils.silentRead;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

public class ApprovedAndReceivedPaths {
    public final Path approvedFile;
    public final Path receivedFile;


    public ApprovedAndReceivedPaths(Path approvedFile, Path receivedFile) {
        this.approvedFile = approvedFile;
        this.receivedFile = receivedFile;
    }

    public boolean haveSameContent() {
        String receivedContent = silentRead(receivedFile);
        String approvedContent = silentRead(approvedFile);
        return receivedContent.equals(approvedContent);
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
        try {
            Files.createDirectories(approvedFile);
            return Files
                .find(approvedFile, MAX_DEPTH, isAnApprovedFile)
                .collect(toList());
        } catch (final IOException e) {
            throw new RuntimeException(format("cannot browse %s for approved files", approvedFile), e);
        }
    }
}
