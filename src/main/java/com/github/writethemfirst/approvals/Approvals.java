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
import com.github.writethemfirst.approvals.utils.FileUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static com.github.writethemfirst.approvals.utils.FileUtils.*;
import static com.github.writethemfirst.approvals.utils.StackUtils.callerClass;
import static com.github.writethemfirst.approvals.utils.StackUtils.callerMethod;
import static java.nio.file.Paths.get;
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

    /**
     * Internal object which allows to hold context information and retrieve the approval files for that context.
     */
    private final Approbation approbation;

    /**
     * The Reporter to be used to report any mismatches while computing the files comparisons.
     */
    private final Reporter reporter;
    private final Path folder;
    private final Class<?> testClass;

    /**
     * Constructs an `Approvals` object using
     *
     * - the default {@link Reporter} ({@link Generic#DEFAULT})
     *
     * - the {@link com.github.writethemfirst.approvals.utils.StackUtils#callerClass(Class)}.
     */
    public Approvals() {
        this(Reporter.DEFAULT);
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
        this(clazz, Reporter.DEFAULT);
    }

    /**
     * Constructs an `Approvals` object.
     *
     * @param clazz    The calling test class. It is used in order to compute the *approved* files' names.
     * @param reporter The {@link Reporter} to trigger in case of differences between the output and an approved file's
     *                 content.
     */
    public Approvals(final Class<?> clazz, final Reporter reporter) {
        this.testClass = clazz;
        folder = folderForClass(clazz);
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
        verify(output, approvedAndReceived(callerMethodName()));
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
        verify(output, approvedAndReceived(customFileName));
    }

    private void verify(final Object output, final ApprovedAndReceivedPaths files) {
        write(output.toString(), files.receivedFile);
        init(files.approvedFile);
        if (files.haveSameContent()) {
            silentRemove(files.receivedFile);
        } else {
            reporter.mismatch(files.approvedFile, files.receivedFile);
            new ThrowsReporter().mismatch(files.approvedFile, files.receivedFile);
        }
    }

    private ApprovedAndReceivedPaths approvedAndReceived(String methodName) {
        return new ApprovedAndReceivedPaths(path(methodName, "approved"), path(methodName, "received"));
    }

    private Path path(String methodName, String extension) {
        return Paths.get(folder.resolve(methodName) + "." + extension);
    }

    /**
     * Computes and returns the Path to the folder to be used for storing the *approved* and *received* files linked to
     * the `testClass` instance.
     *
     * The folder will be created under `src/test/resources` in the really same project, and will be named after the
     * package name of the `testClass`, followed by the name of the `testClass` itself. That folder will later contain
     * one pair of files (*approved* and *received*) for each method to be tested.
     *
     * @return The Path to the folder linked to the `testClass` attribute, used for storing the *received* and
     * *approved* files.
     */
    private static Path folderForClass(Class<?> testClass) {
        final String packageName = testClass.getPackage().getName();
        final Path packageResourcesPath = get("src/test/resources/", packageName.split("\\."));
        return packageResourcesPath.resolve(testClass.getSimpleName());
    }


    /**
     * Returns the caller method name using {@link com.github.writethemfirst.approvals.utils.StackUtils}.
     *
     * It returns `unknown_method` in case the caller method cannot be retrieved automatically.
     *
     * Info: It doesn't return an option or null or an empty string, so the generated files are located in a visible
     * *unknown* file, which encourages the developer to solve the actual issue.
     *
     * @return the caller method name found by {@link com.github.writethemfirst.approvals.utils.StackUtils} or
     * `unknown_method` otherwise.
     */
    private String callerMethodName() {
        return callerMethod(testClass).orElse("unknown_method");
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
