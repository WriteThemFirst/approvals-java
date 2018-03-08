package com.github.writethemfirst.approvals.files;

import com.github.writethemfirst.approvals.utils.FileUtils;

import java.nio.file.Path;

import static com.github.writethemfirst.approvals.utils.FileUtils.silentRead;
import static com.github.writethemfirst.approvals.utils.FileUtils.silentRemove;

public abstract class CommonFile {
    public abstract Path get();

    public abstract Path get(Path relativeFile);

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
     * The custom *received* file will be retrieved from the approvalsFolder by searching for the provided
     * relativeFile in it. Once found, the specified content will be written in it.
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
}
