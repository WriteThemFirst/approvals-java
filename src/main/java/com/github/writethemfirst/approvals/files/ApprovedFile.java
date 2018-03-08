package com.github.writethemfirst.approvals.files;

import com.github.writethemfirst.approvals.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static com.github.writethemfirst.approvals.utils.FileUtils.silentRead;
import static com.github.writethemfirst.approvals.utils.FileUtils.silentRemove;
import static java.lang.String.format;

/**
 * # ApprovedFile
 *
 * In *Approval Testing*, the *approved* file is the one containing the reference data to be validated while computing
 * the tests. It is generated in a first place by the *Program Under Tests* and reviewed by the developer. Once
 * approved, it must be committed along with the source code of the project since it actually contains all the data to
 * be used for the tests assertions.
 *
 * This class aims at providing all the necessary methods for manipulating a particular *approved* file: from reading to
 * writing it.
 *
 * @author aneveux
 * @version 1.0
 * @since 1.1
 */
public class ApprovedFile extends CommonFile {

    /**
     * Extension to be used for all approved files.
     */
    public static final String APPROVED_EXTENSION = "approved";
    private final ApprovalsFiles approvalsFiles;

    public ApprovedFile(ApprovalsFiles approvalsFiles) {
        this.approvalsFiles = approvalsFiles;
    }

    /**
     * Returns the *approved* file Path linked to the specified `methodName`.
     *
     * The Path will be computed by adding the APPROVED_EXTENSION to the provided `methodName` and search for it in the
     * `folder` associated with the `testClass`.
     *
     * @return The Path to the *approved* file linked to the provided `methodName`.
     */
    @Override
    public Path get() {
        final String fileName = String.format("%s.%s", approvalsFiles.methodName, APPROVED_EXTENSION);
        return approvalsFiles.folder.resolve(fileName);
    }

    /**
     * Returns the Path of the *approved* file computed from the current approvalsFolder and the provided relativeFile
     * path. Which means it'll resolve automatically the approvals folder path, and then simply resolve the relativeFile
     * path within it.
     *
     * @param relativeFile The path of the *approved* file to search for in the current approvals folder
     * @return The complete path of the *approved* file found in the current approvals folder
     */
    @Override
    public Path get(final Path relativeFile) {
        return approvalsFiles.approvalsFolder().resolve(format("%s.%s", relativeFile, APPROVED_EXTENSION));
    }


    /**
     * Creates an empty *approved* file if it doesn't exist yet.
     *
     * If it already exist, that method will do nothing. If there's any issue while creating the *approved* file, the
     * {@link IOException} will be wrapped in a {@link RuntimeException} and thrown.
     */
    public void init() {
        final File approvedFile = get().toFile();
        if (!approvedFile.exists()) {
            try {
                //noinspection ResultOfMethodCallIgnored
                approvedFile.createNewFile();
            } catch (final IOException e) {
                throw new RuntimeException(format("Could not create empty approved file <%s>", approvedFile), e);
            }
        }
    }
}
