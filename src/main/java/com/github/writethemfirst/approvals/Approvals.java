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

import com.github.writethemfirst.approvals.reporters.ThrowsReporter;
import com.github.writethemfirst.approvals.reporters.softwares.Generic;
import javafx.util.Pair;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static com.github.writethemfirst.approvals.utils.FileUtils.silentRead;
import static com.github.writethemfirst.approvals.utils.StackUtils.callerClass;
import static java.lang.String.format;

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
     * Constructs an `Approvals` object using the default {@link Reporter} ({@link Generic#DEFAULT}).
     *
     * @param clazz The calling test class. It is used in order to compute the *approved* files' names.
     */
    public Approvals(final Class<?> clazz) {
        this(clazz, Generic.DEFAULT);
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
        verify(output, approvalsFiles.defaultContext());
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
     * This method is useful for non Java test frameworks (ScalaTest, KotlinTest ...) where methodName cannot be
     * inferred from the stack.
     *
     * @param output     Any object with a {@link Object#toString()} representation containing the output of your
     *                   program. It will be compared to the associated *approved* file.
     * @param methodName specifies the caller method name, which is used to name the *approved* and *received* files.
     * @throws AssertionError   if the {@link Reporter} implementation relies on standard assertions provided by a
     *                          framework like JUnit
     * @throws RuntimeException if the {@link Reporter} relies on executing an external command which failed
     */
    public void verify(final Object output, final String methodName) {
        verify(output, approvalsFiles.context(methodName));
    }

    private void verify(Object output, ApprobationContext context) {
        if (matchesApprovedFile(output, context)) {
            context.removeReceived();
        } else {
            context.createEmptyApprovedFileIfEmpty();
            reporter.mismatch(context.approvedFile(), context.receivedFile());
            new ThrowsReporter().mismatch(context.approvedFile(), context.receivedFile());
        }
    }


    /**
     * Compares the *Program Under Tests*' output to the content of the *approved* file and checks for any differences.
     *
     * @param output  Any object representing the output of *Program  Under Tests*. A `String` representation of that
     *                object will be computed using `toString()` and will be used for the comparison with the *approved*
     *                file's content.
     * @param context
     * @return true if the provided output perfectly matches with the existing *approved* file
     */
    private boolean matchesApprovedFile(final Object output, ApprobationContext context) {
        final String approvedContent = context.readApproved();
        context.writeReceived(output.toString());
        return approvedContent != null && approvedContent.equals(output.toString());
    }

    public void verifyAgainstMasterFolder(Path actualFolder) {
        ApprobationContext context = approvalsFiles.defaultContext();

        Path approvedFolder = context.approvedFolder();
        List<Pair<Path, Path>> mismatches = new ArrayList<>();
        for (Path approvedFile : context.approvedFilesInFolder()) {
            Path approvedRelative = approvedFolder.relativize(approvedFile);
            Path simplePath = Paths.get(approvedRelative.toString().replace(".approved", ""));
            Path actualFile = actualFolder.resolve(simplePath);
            Path receivedFile = approvedFolder.resolve(simplePath + ".received");
//        TODO:    FileUtils.copy(actualFile, receivedFile);
            if (!actualFile.toFile().exists()) {
                throw new AssertionError(format("missing file <%s> in <%s>", simplePath, actualFolder));
            }
            String receivedContent = silentRead(actualFile);
            String approvedContent = silentRead(approvedFile);
            if (!receivedContent.equals(approvedContent)) {
                mismatches.add(new Pair<>(approvedFile, receivedFile));
            }
        }
        for (Pair<Path, Path> mismatch : mismatches) {
            reporter.mismatch(mismatch.getKey(), mismatch.getValue());
        }
        for (Pair<Path, Path> mismatch : mismatches) {
            throw new AssertionError(format(
                "compared to reference <%s>, content differs for file <%s>", mismatch.getKey(), mismatch.getValue()));
        }
    }
}
