package com.github.writethemfirst.approvals.files;

import java.nio.file.Path;

import static java.lang.String.format;

/**
 * # ReceivedFile
 *
 * In *Approval Testing*, the *received* file is a temporary file which is generated from the output of the *Program
 * Under Test* and used for comparing the results with the reference of the *approved* file. That file will then be
 * deleted to avoid polluting the codebase. In case of the tests failing, the file will most likely be kept for
 * further review by the developer.
 *
 * This class aims at providing all the necessary methods for manipulating a particular *received* file: from
 * reading to writing it.
 *
 * @author aneveux
 * @version 1.0
 * @since 1.1
 */
public class ReceivedFile extends CommonFile {

    /**
     * Extension to be used for all received files.
     */
    public static final String RECEIVED_EXTENSION = "received";
    private final ApprovalsFiles approvalsFiles;

    public ReceivedFile(ApprovalsFiles approvalsFiles) {

        this.approvalsFiles = approvalsFiles;
    }

    /**
     * Returns the *received* file Path linked to the specified `methodName`.
     *
     * The Path will be computed by adding the RECEIVED_EXTENSION to the provided `methodName` and search for it in
     * the `folder` associated with the `testClass`.
     *
     * @return The Path to the *received* file linked to the provided `methodName`.
     */
    public Path get() {
        final String fileName = format("%s.%s", approvalsFiles.methodName, RECEIVED_EXTENSION);
        return approvalsFiles.folder.resolve(fileName);
    }

    /**
     * Returns the Path of the *received* file computed from the current approvalsFolder and the provided
     * relativeFile path. Which means it'll resolve automatically the approvals folder path, and then simply resolve
     * the relativeFile path within it.
     *
     * @param relativeFile The path of the *received* file to search for in the current approvals folder
     * @return The complete path of the *received* file found in the current approvals folder
     */
    public Path get(final Path relativeFile) {
        return approvalsFiles.approvalsFolder().resolve(format("%s.%s", relativeFile, RECEIVED_EXTENSION));
    }



}
