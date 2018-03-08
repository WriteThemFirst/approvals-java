package com.github.writethemfirst.approvals.files;

import com.github.writethemfirst.approvals.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static com.github.writethemfirst.approvals.utils.FileUtils.silentRead;
import static com.github.writethemfirst.approvals.utils.FileUtils.silentRemove;
import static java.lang.String.format;

public class ApprovedOrReceivedFile {
    public static final String APPROVED_EXTENSION = "approved";
    private final ApprovalsFiles approvalsFiles;
    private final String extension;

    ApprovedOrReceivedFile(ApprovalsFiles approvalsFiles, String extension) {
        this.approvalsFiles = approvalsFiles;
        this.extension = extension;
    }

    /**
     * Returns the Path of the *approved* file computed from the current approvalsFolder and the provided relativeFile
     * path. Which means it'll resolve automatically the approvals folder path, and then simply resolve the relativeFile
     * path within it.
     *
     * @param relativeFile The path of the *approved* file to search for in the current approvals folder
     * @return The complete path of the *approved* file found in the current approvals folder
     */
    public Path get(final Path relativeFile) {
        return approvalsFiles.approvalsFolder().resolve(format("%s.%s", relativeFile, extension));
    }


    /**
     * Returns the *approved* file Path linked to the specified `methodName`.
     *
     * The Path will be computed by adding the EXTENSION to the provided `methodName` and search for it in the `folder`
     * associated with the `testClass`.
     *
     * @return The Path to the *approved* file linked to the provided `methodName`.
     */
    public Path get() {
        final String fileName = String.format("%s.%s", approvalsFiles.methodName, extension);
        return approvalsFiles.folder.resolve(fileName);
    }


    /**
     * Reads the content of the *approved* file linked to the current {@link ApprobationContext}.
     *
     * That method will actually read the file which has been determined by the approbation context and will return its
     * content.
     *
     * If the file doesn't exist, that method will simply display a message in `System.err` but won't fail.
     *
     * @return The content of the *approved* file linked to the current {@link ApprobationContext}. An empty String if
     * the file doesn't exist.
     */
    public String read() {
        return silentRead(get());
    }

    /**
     * Reads the content of the custom *approved* file linked to the current {@link ApprobationContext}.
     *
     * The custom *approved* file will be retrieved from the approvalsFolder by searching for the provided relativeFile
     * in it. Once found, its content will be returned.
     *
     * If the file doesn't exist, that method will simply display a message in `System.err` but won't fail.
     *
     * @return The content of the *approved* file linked to the current {@link ApprobationContext}. An empty String if
     * the file doesn't exist.
     */
    public String read(final Path relativeFile) {
        return silentRead(get(relativeFile));
    }

    /**
     * Writes the provided content in the *received* file linked to the current {@link ApprobationContext}.
     *
     * If the file doesn't exist, that method will create it in the `src/test/resources` folder.
     *
     * @param content Content to be written in the *received* file linked to the parent method execution.
     */
    public void write(final String content) {
        FileUtils.write(content, get());
    }

    /**
     * Writes the provided content in the custom *received* file linked to the current {@link ApprobationContext}.
     *
     * The custom *received* file will be retrieved from the approvalsFolder by searching for the provided relativeFile
     * in it. Once found, the specified content will be written in it.
     *
     * If the file doesn't exist, that method will create it in the `src/test/resources` folder.
     *
     * @param content Content to be written in the *received* file linked to the parent method execution.
     */
    public void write(final String content, final Path relativeFile) {
        FileUtils.write(content, get(relativeFile));
    }

    /**
     * Removes the *received* file linked to the current {@link ApprobationContext}.
     *
     * If the file doesn't exist, it won't do anything and won't return any kind of error.
     */
    public void remove() {
        silentRemove(get());
    }

    /**
     * Removes the custom *approved* file linked to the current {@link ApprobationContext}.
     *
     * The custom *approved* file will be retrieved from the approvalsFolder by searching for the provided relativeFile
     * in it. Once found, it'll be removed.
     *
     * If the file doesn't exist, it won't do anything and won't return any kind of error.
     */
    public void remove(final Path relativeFile) {
        silentRemove(get(relativeFile));
    }

    /**
     * Creates an empty *approved* file if it doesn't exist yet.
     *
     * If it already exist, that method will do nothing. If there's any issue while creating the *approved* file, the
     * {@link IOException} will be wrapped in a {@link RuntimeException} and thrown.
     */
    public void init() {
        final File file = get().toFile();
        if (!file.exists()) {
            try {
                //noinspection ResultOfMethodCallIgnored
                file.createNewFile();
            } catch (final IOException e) {
                throw new RuntimeException(format("Could not create empty file <%s>", file), e);
            }
        }
    }
}
