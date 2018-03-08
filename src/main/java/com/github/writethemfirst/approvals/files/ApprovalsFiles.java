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

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

/**
 * # ApprovalsFiles
 *
 * *Approval Testing* relies on comparing data produced by a *Program Under Tests* and data which has been reviewed and
 * validated by the developer. This validated data is stored in *approved* files along with the project, and is used for
 * comparisons with the data produced by the program's execution.
 *
 * Two files are needed for computing *Approval Tests*: an *approved* file, which should be committed with the source
 * code, containing data validated by the developer, and a *received* file, which temporarily holds the data produced by
 * the *Program Under Tests* execution. Those files are then compared to validate the proper behavior of the program.
 *
 * That `ApprovalsFiles` class can be seen as a wrapper of the *approved* and *received* files for a particular {@link
 * ApprobationContext} since they allow to manage both files for a particular class and method. That wrapper class will
 * provide all the necessary methods to manage the approbation files.
 *
 * `ApprovalsFiles` won't automatically name the files but will only rely on the folder and methodName arguments it
 * received while being created.
 *
 * @author mdaviot / aneveux
 * @version 1.1
 */
public class ApprovalsFiles {

    /**
     * The folder in which the *approved* and *received* files are supposed to be created.
     *
     * It'll usually match with the test class name.
     */
    final Path folder;

    /**
     * The caller test method name which will be used for naming the *approved* and *received* files.
     */
    final String methodName;

    /**
     * Instance of the {@link ApprovedFile} linked to that {@link ApprovalsFiles} instance.
     */
    final public ApprovedFile approved;

    /**
     * Instance of the {@link ReceivedFile} linked to that {@link ApprovalsFiles} instance.
     */
    final public ReceivedFile received;

    /**
     * Constructs an {@link ApprovalsFiles} instance linked to the specified folder and methodName.
     *
     * @param folder     The folder in which the *approved* and *received* files are supposed to be created.
     * @param methodName The caller test method name which will be used for naming the *approved* and *received* files.
     */
    public ApprovalsFiles(final Path folder, final String methodName) {
        this.folder = folder;
        this.methodName = methodName;
        approved = new ApprovedFile(this);
        received = new ReceivedFile(this);
    }

    /**
     * Returns the path to a dedicated folder for the current {@link ApprobationContext}.
     *
     * That folder may be used for storing multiple files which will later be compared for approval.
     *
     * The folder name will be the method name followed by `.Files`.
     *
     * @return The path to a folder dedicated to storing the approvals files of the current context.
     */
    public Path approvalsFolder() {
        final String folderName = format("%s.Files", methodName);
        return folder.resolve(folderName);
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
            attributes.isRegularFile() && path.toString().endsWith("." + ApprovedFile.APPROVED_EXTENSION);
        final Path approvalsFolder = approvalsFolder();
        try {
            Files.createDirectories(approvalsFolder);
            return Files
                .find(approvalsFolder, MAX_DEPTH, isAnApprovedFile)
                .collect(toList());
        } catch (final IOException e) {
            throw new RuntimeException(format("cannot browse %s for approved files", approvalsFolder), e);
        }
    }

}
