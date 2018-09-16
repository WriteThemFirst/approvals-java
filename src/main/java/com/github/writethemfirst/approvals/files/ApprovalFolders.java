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
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.github.writethemfirst.approvals.utils.FileUtils.copyToFolder;
import static com.github.writethemfirst.approvals.utils.FileUtils.listFiles;
import static java.util.stream.Collectors.partitioningBy;

/**
 * ApprovalFolders is similar to ApprovalFiles : it holds a pair of {@link Path}s, *approved* and *received*.
 *
 * The difference is that these paths represents folders we are comparing.
 */
public class ApprovalFolders extends ApprovalFiles {

    /**
     * Constructs a pair of approval entries from the provided folder and method name. The path for both *approved* and
     * *received* files will be computed and used as approval files.
     *
     * @param folder     The folder in which the approval files will be located
     * @param methodName The name of the method calling the test. It is used to actually name the approval files
     */
    public ApprovalFolders(final Path folder, final String methodName) {
        super(folder, methodName);
    }

    ApprovalFolders(final Path approved, final Path received) {
        super(approved, received);
    }


    /**
     * Appends to the approved and received paths the name of a file.
     *
     * @param file the Path for which only the name is taken into account
     * @return an ApprovalsFile, one step deeper
     */
    public ApprovalFiles resolve(final Path file) {
        final Path fileName = file.getFileName();
        return new ApprovalFiles(
            approved.resolve(fileName),
            received.resolve(fileName));
    }

    /**
     * Creates the *approved* folder and copies files from *actual* to *received* folder.
     */
    public void prepareFolders(final Path actualFolder) {
        try {
            Files.createDirectories(approved);
        } catch (final IOException e) {
            throw new RuntimeException("could not create *approved* folder " + approved, e);
        }
        listFiles(actualFolder).forEach(actual -> copyToFolder(actual, received));
    }


    /**
     * Compares the content of files in *approved* and *received* folders (only makes sense if *approved* and *received*
     * are folders).
     *
     * @return the 2 lists of matches (files with same content) and mismatches (different files)
     */
    public MatchesAndMismatches matchesAndMismatches() {
        final Map<Boolean, List<ApprovalFiles>> matchesAndMismatches = Stream
            .concat(
                listFiles(approved).map(this::matchApprovedFile),
                listFiles(received).map(this::matchReceivedFile))
            .distinct()
            .collect(partitioningBy(ApprovalFiles::haveSameContent));

        return new MatchesAndMismatches(
            matchesAndMismatches.get(true),
            matchesAndMismatches.get(false));
    }


    /**
     * If approved and received are directories, this method allows to create a new {@link ApprovalFiles} object which
     * associates to an already known *approved* file a resolved *received* file. That *received* file will be searched
     * for in the received folder by resolving the relative path found from the *approved* file.
     *
     * It allows to go from a found *approved* file to a pair of both *approved* and *received* file.
     *
     * If approved or received are not directories (which is checked with areRegularFiles), this current instance of
     * {@link ApprovalFiles} will be returned.
     *
     * @param approvedFile The *approved* file we already know and for which we want to associate a *received* file
     * @return An {@link ApprovalFiles} instance containing both the *approved* and matching *received* file
     */
    private ApprovalFiles matchApprovedFile(final Path approvedFile) {
        return new ApprovalFiles(approvedFile, changeRoot(approvedFile, approved, received));
    }


    /**
     * If approved and received are directories, this method allows to create a new {@link ApprovalFiles} object which
     * associates to an already known *received* file a resolved *approved* file. That *approved* file will be searched
     * for in the approved folder by resolving the relative path found from the *received* file.
     *
     * It allows to go from a found *received* file to a pair of both *approved* and *received* file.
     *
     * If approved or received are not directories (which is checked with areRegularFiles), this current instance of
     * {@link ApprovalFiles} will be returned.
     *
     * @param receivedFile The *received* file we already know and for which we want to associate a *approved* file
     * @return An {@link ApprovalFiles} instance containing both the *approved* and matching *received* file
     */
    private ApprovalFiles matchReceivedFile(final Path receivedFile) {
        return new ApprovalFiles(changeRoot(receivedFile, received, approved), receivedFile);
    }

    private Path changeRoot(final Path file, final Path initialRoot, final Path newRoot) {
        return newRoot.resolve(initialRoot.relativize(file));
    }
}
