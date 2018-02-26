package com.github.writethemfirst.approvals;

import com.github.writethemfirst.approvals.reporters.softwares.Generic;

import java.nio.file.Path;
import java.nio.file.Paths;

import static com.github.writethemfirst.approvals.utils.FileUtils.silentRead;
import static com.github.writethemfirst.approvals.utils.StackUtils.callerClass;

/**
 * # Approvals
 *
 * *Approval Testing* is a way of considering Unit Tests without focusing on writing tons of assertions, but by letting
 * the framework generate the references to use in assertions.
 *
 * `Approvals` is this framework's main entry point for validation of data produced by your program, by comparing it to
 * data you already reviewed and approved manually.
 *
 * Using `Approvals` for checking your program's data will actually trigger a comparison between the output it produces,
 * and content stored in a file somewhere in your project's tests. If that file doesn't exist, the framework will warn
 * you and propose you to review the data produced by your program to approve (or not) what has been generated. After
 * you reviewed it and selected the proper output, it'll use the resulting file for future comparisons.
 *
 * An `Approvals` object can be constructed with:
 *
 * - a {@link Class} to be linked with the object. It is used in order to automatically compute the path and name of the
 * *approved* files, used to store the output data of your program.
 *
 * - a {@link Reporter}, to be used if differences are found between your program's output and the *approved* file's
 * content. Some `Reporter`s are provided by the framework, but you can also provide your own.
 *
 * The basic and main entry point you should have a look at in this `Approvals` class is the {@link
 * Approvals#verify(Object)} method, allowing to compute a comparison between an output and some content stored in an
 * *approved* file.
 *
 * @author mdaviot / aneveux
 * @version 1.0
 * @see Reporter
 */
public class Approvals {

    private final ApprovalsFiles approvalsFiles;
    private final Reporter reporter;

    /**
     * Constructs an `Approvals` object using
     *
     * - the default {@link Reporter} ({@link Generic#DEFAULT})
     *
     * - the {@link com.github.writethemfirst.approvals.utils.StackUtils#callerClass(Class)}.
     */
    public Approvals() {
        this(Generic.DEFAULT);
    }

    /**
     * Constructs an `Approvals` object using the {@link com.github.writethemfirst.approvals.utils.StackUtils#callerClass(Class)}.
     *
     * @param reporter The {@link Reporter} to trigger in case of differences between the output and an approved file's
     *                 content.
     */
    public Approvals(final Reporter reporter) {
        this(callerClass(Approvals.class), reporter);
    }


    /**
     * Constructs an `Approvals` object.
     *
     * @param clazz    The calling test class. It is used in order to compute the *approved* files' names.
     * @param reporter The {@link Reporter} to trigger in case of differences between the output and an approved file's
     *                 content.
     */
    public Approvals(final Class<?> clazz, final Reporter reporter) {
        approvalsFiles = new ApprovalsFiles(clazz);
        this.reporter = reporter;
    }


    /**
     * Compares the actual output of your program (the function's argument) and the content of the *approved* file
     * matching with the test method.
     *
     * It'll use a temporary *received* file to store the output of your program. This file will be erased in case the
     * results are matching. Otherwise, it will be kept for you to review it.
     *
     * In case of differences found in the output, the {@link Reporter} linked to this `Approvals` instance will be
     * called ({@link Reporter#mismatch(Path, Path)}).
     *
     * @param output Any object with a {@link Object#toString()} representation containing the output of your program.
     *               It will be compared to the associated *approved* file.
     * @throws AssertionError   if the {@link Reporter} implementation relies on standard assertions provided by a
     *                          framework like JUnit
     * @throws RuntimeException if the {@link Reporter} relies on executing an external command which failed
     */
    public void verify(final Object output) {
        if (matchesApprovedFile(output)) {
            approvalsFiles.removeReceived();
        } else {
            reporter.mismatch(approvalsFiles.approvedFile(), approvalsFiles.receivedFile());
        }
    }


    /**
     * Compares the *Program Under Tests*' output to the content of the *approved* file and checks for any differences.
     *
     * @param output Any object representing the output of *Program  Under Tests*. A `String` representation of that
     *               object will be computed using `toString()` and will be used for the comparison with the *approved*
     *               file's content.
     * @return true if the provided output perfectly matches with the existing *approved* file
     */
    private boolean matchesApprovedFile(final Object output) {
        final String approvedContent = approvalsFiles.readApproved();
        approvalsFiles.writeReceived(output.toString());
        return approvedContent != null && approvedContent.equals(output.toString());
    }

    public void verifyAgainstMasterFolder(Path receivedFolder) {
        Path approvedFolder = approvalsFiles.approvedFolder();
        for (Path approvedFile : approvalsFiles.approvedFilesInFolder()) {
            Path approvedRelative = approvedFolder.relativize(approvedFile);
            Path simplePath = Paths.get(approvedRelative.toString().replace(".approved", ""));
            Path receivedFile = receivedFolder.resolve(simplePath);
            if (!receivedFile.toFile().exists()) {
                throw new AssertionError(String.format("missing file <%s> in <%s>", simplePath, receivedFolder));
            }
            String receivedContent = silentRead(receivedFile);
            String approvedContent = silentRead(approvedFile);
            if (!receivedContent.equals(approvedContent)) {
                throw new AssertionError(String.format(
                    "compared to reference <%s>, content differs for file <%s>", approvedRelative, receivedFile));
            }
        }
    }
}
