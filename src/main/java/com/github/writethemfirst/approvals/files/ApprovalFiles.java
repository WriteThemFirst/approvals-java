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
public class ApprovalFiles extends ApprovedAndReceived {

    /**
     * Constructs an ApprovalFiles instance for a pair of *approved* and *received* entries.
     *
     * @param approved         An *approved* entry (can be either a file or a folder)
     * @param received         A *received* entry (can be either a file or a folder)
     */
    public ApprovalFiles(final Path approved, final Path received) {
        super(approved, received);
    }

    /**
     * Constructs a pair of approval entries from the provided folder and method name. The path for both *approved* and
     * *received* files will be computed and used as approval files.
     *
     * @param folder     The folder in which the approval files should be located
     * @param methodName The name of the method calling the test. It is used to actually name the approval files
     */
    public ApprovalFiles(final Path folder, final String methodName) {
        super(folder, methodName);
    }

}
