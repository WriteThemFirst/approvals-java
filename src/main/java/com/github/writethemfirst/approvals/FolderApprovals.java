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
package com.github.writethemfirst.approvals;

import com.github.writethemfirst.approvals.files.ApprovedAndReceivedPaths;
import com.github.writethemfirst.approvals.utils.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static com.github.writethemfirst.approvals.utils.FileUtils.*;
import static java.util.stream.Collectors.partitioningBy;

/**
 * Approves a folder. See {@link #verifyAllFiles(Path)}.
 */
public class FolderApprovals extends Approvals {

    /**
     * Standard FolderApprovals.
     */
    public FolderApprovals() {
    }

    /**
     * Specifies the reporter used to report mismatches.
     *
     * @return a copy of this FolderApprovals
     */
    public FolderApprovals reportTo(final Reporter reporter) {
        return new FolderApprovals(reporter, customFileName, customExtension);
    }

    /**
     * Specifies the name to use for *approved* and *received* files.
     *
     * @return a copy of this FolderApprovals
     */
    public FolderApprovals writeTo(final String customFileName) {
        return new FolderApprovals(reporter, customFileName, customExtension);
    }

    private FolderApprovals(
        final Reporter reporter,
        final String customFileName,
        final String customExtension) {

        super(reporter, customFileName, customExtension);
    }

    /**
     * Compares the actual output of your program (files in the folder `actualFolder`) and the content of the *approved*
     * "Master" folder matching with the test method.
     *
     * It'll use a temporary *received* folder to copy the actual files from your program. This folder and its files
     * will be erased in case the results are matching. Otherwise, they will be kept for you to review it.
     *
     * In case of differences found in the output, the {@link Reporter} linked to this `Approvals` instance will be
     * called ({@link Reporter#mismatch(Path, Path)}) for each mismatched file.
     *
     * @param actualFolder the folder containing the output of your program. It will be compared to the associated
     *                     *approved* folder
     * @throws AssertionError   if the {@link Reporter} implementation relies on standard assertions provided by a
     *                          framework like JUnit
     * @throws RuntimeException if the {@link Reporter} relies on executing an external command which failed
     */
    public void verifyAllFiles(final Path actualFolder) {
        final ApprovedAndReceivedPaths approvedAndReceivedPaths = approvedAndReceivedPaths();
        prepareFolders(actualFolder, approvedAndReceivedPaths);
        final Map<Boolean, List<ApprovedAndReceivedPaths>> matchesAndMismatches =
            approvedAndReceivedPaths
                .allFilesToCheck()
                .collect(partitioningBy(ApprovedAndReceivedPaths::filesHaveSameContent));

        cleanupReceivedFiles(approvedAndReceivedPaths, matchesAndMismatches);
        reportMismatches(matchesAndMismatches);
    }

    private void reportMismatches(final Map<Boolean, List<ApprovedAndReceivedPaths>> matchesAndMismatches) {
        matchesAndMismatches.get(false).forEach(mismatch -> reporter.mismatch(mismatch.approved, mismatch.received));
    }

    private void cleanupReceivedFiles(final ApprovedAndReceivedPaths approvedAndReceivedPaths, final Map<Boolean, List<ApprovedAndReceivedPaths>> matchesAndMismatches) {
        matchesAndMismatches.get(true).forEach(ar -> silentRemove(ar.received));
        if (matchesAndMismatches.get(false).isEmpty()) {
            silentRecursiveRemove(approvedAndReceivedPaths.received);
        }
    }

    /**
     * Copies files from *actual* to *received* folder, and creates missing *approved* files.
     */
    private void prepareFolders(final Path actualFolder, final ApprovedAndReceivedPaths approvedAndReceivedPaths) {
        try {
            Files.createDirectories(approvedAndReceivedPaths.approved);
        } catch (final IOException e) {
            throw new RuntimeException("could not create *approved* folder " + approvedAndReceivedPaths.approved, e);
        }
        searchFiles(actualFolder).forEach(p -> FileUtils.copyToFolder(p, approvedAndReceivedPaths.received));
        approvedAndReceivedPaths
            .allFilesToCheck()
            .map(paths -> paths.approved)
            .forEach(FileUtils::createFile);
    }
}
