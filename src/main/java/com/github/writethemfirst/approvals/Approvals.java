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

import com.github.writethemfirst.approvals.files.Approbation;
import com.github.writethemfirst.approvals.files.ApprovalsFiles;
import com.github.writethemfirst.approvals.files.ApprovedAndReceivedPaths;
import com.github.writethemfirst.approvals.reporters.ThrowsReporter;
import com.github.writethemfirst.approvals.reporters.softwares.Generic;
import com.github.writethemfirst.approvals.utils.FileUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static com.github.writethemfirst.approvals.utils.FileUtils.silentRemove;
import static com.github.writethemfirst.approvals.utils.StackUtils.callerClass;
import static java.util.stream.Collectors.partitioningBy;

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

    private final Approbation approbation;
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
        approbation = new Approbation(clazz);
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
        verify(output, approbation.defaultFiles());
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
     * This method is useful for non Java test frameworks (ScalaTest, KotlinTest ...) where the method name cannot be
     * inferred from the stack.
     *
     * @param output         Any object with a {@link Object#toString()} representation containing the output of your
     *                       program. It will be compared to the associated *approved* file.
     * @param customFileName specifies the approval test file name which is used for both the *approved* and *received*
     *                       files.
     * @throws AssertionError   if the {@link Reporter} implementation relies on standard assertions provided by a
     *                          framework like JUnit
     * @throws RuntimeException if the {@link Reporter} relies on executing an external command which failed
     */
    public void verify(final Object output, final String customFileName) {
        verify(output, approbation.customFiles(customFileName));
    }

    /**
     * That method actually computes the logic behind *Approval Testing*.
     *
     * It'll write the output of the *Program Under Test* in a *received* file and then compare it to the existing
     * *approved* file. If they do match, it'll remove the *received* file and won't do anything. Otherwise, the
     * *received* file will be kept for later review by the developer, while the reporter will be used in order to
     * report a mismatch. An empty *approved* file will be intiated as well.
     *
     * @param output Output object of the *Program Under Tests* which will be used for comparison with the *approved*
     *               file
     * @param files  Wrapper allowing to get the approvals files in the current execution context
     */
    private void verify(final Object output, final ApprovalsFiles files) {
        writeReceivedFile(output, files);
        if (files.haveSameContent()) {
            files.received.remove();
        } else {
            files.approved.init();
            reporter.mismatch(files.approved.get(), files.received.get());
            new ThrowsReporter().mismatch(files.approved.get(), files.received.get());
        }
    }

    /**
     * Writes a String representation of the provided output in the *received* file linked to the current test
     * execution. The `toString` method of the provided object will be used for writting its content in the file.
     *
     * @param output Output object of the *Program Under Tests* which will be used to write the *received* file
     * @param files  Wrapper allowing to get the approvals files in the current execution context
     */
    private void writeReceivedFile(final Object output, final ApprovalsFiles files) {
        files.received.write(output.toString());
    }


    public void verifyAgainstMasterFolder(Path actualFolder) {
        ApprovalsFiles context = approbation.defaultFiles();

        Path approvedFolder = context.approvalsFolder();
        Map<Boolean, List<ApprovedAndReceivedPaths>> matchesAndMismatches =
            context.approvedFilesInFolder()
                .stream()
                .map(approvedFile -> approvedAndReceived(actualFolder, approvedFolder, approvedFile))
                .collect(partitioningBy(ar -> ar.haveSameContent()));

        matchesAndMismatches.get(true).forEach(ar -> silentRemove(ar.receivedFile));

        handleMismatches(matchesAndMismatches.get(false));
    }

    private void handleMismatches(List<ApprovedAndReceivedPaths> mismatches) {
        mismatches.forEach(mismatch -> reporter.mismatch(mismatch.approvedFile, mismatch.receivedFile));
        mismatches.forEach(mismatch -> new ThrowsReporter().mismatch(mismatch.approvedFile, mismatch.receivedFile));
    }

    private ApprovedAndReceivedPaths approvedAndReceived(Path actualFolder, Path approvedFolder, Path approvedFile) {
        Path approvedRelative = approvedFolder.relativize(approvedFile);
        Path simplePath = Paths.get(approvedRelative.toString().replace(".approved", ""));
        Path actualFile = actualFolder.resolve(simplePath);
        Path receivedFile = approvedFolder.resolve(simplePath + ".received");
        FileUtils.copy(actualFile, receivedFile);
        return new ApprovedAndReceivedPaths(approvedFile, receivedFile);
    }
}
