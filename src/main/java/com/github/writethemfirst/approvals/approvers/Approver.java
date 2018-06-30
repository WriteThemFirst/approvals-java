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
package com.github.writethemfirst.approvals.approvers;

import com.github.writethemfirst.approvals.Approvals;
import com.github.writethemfirst.approvals.Reporter;
import com.github.writethemfirst.approvals.files.ApprovalFiles;
import com.github.writethemfirst.approvals.reporters.ThrowsReporter;

import java.nio.file.Path;

import static com.github.writethemfirst.approvals.files.ApprovalFiles.build;
import static com.github.writethemfirst.approvals.utils.FileUtils.*;
import static com.github.writethemfirst.approvals.utils.StackUtils.callerClass;
import static com.github.writethemfirst.approvals.utils.StackUtils.callerMethod;
import static java.nio.file.Paths.get;

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
 * Approver#verify(Object)} method, allowing to compute a comparison between an output and some content stored in an
 * *approved* file.
 *
 * @see Reporter
 */
public class Approver {
    final Class<?> testClass;
    private final Path folder;
    final Reporter reporter;
    final String customFileName;
    final String customExtension;

    /**
     * Standard approvals, with default {@link Reporter} and file name detected from the test class used to apply the
     * constructor and the test method used to apply a {@link #verify(Object)} method.
     */
    public Approver() {
        this(Reporter.DEFAULT,
            null,
            "",
            callerClass(Approver.class, Approvals.class, FolderApprover.class, CombinationApprover.class));
    }

    /**
     * Protected constructor used by the "copy" methods.
     */
    Approver(
        final Reporter reporter,
        final String customFileName,
        final String customExtension,
        final Class<?> testClass) {

        this.reporter = reporter;
        this.customFileName = customFileName;
        this.customExtension = customExtension;
        this.testClass = testClass;
        this.folder = folderForClass(testClass);
    }

    /**
     * Specifies the reporter used to report mismatches.
     *
     * @return a copy of this Approvals
     */
    public Approver reportTo(final Reporter reporter) {
        return new Approver(reporter, customFileName, customExtension, testClass);
    }

    /**
     * Specifies the name to use for *approved* and *received* files.
     *
     * @return a copy of this Approvals
     */
    public Approver writeTo(final String customFileName) {
        return new Approver(reporter, customFileName, customExtension, testClass);
    }

    /**
     * Specifies the testClass to use as a folder name to store *approved* and *received* files.
     *
     * @return a copy of this Approvals
     */
    public Approver testing(final Class<?> testClass) {
        return new Approver(reporter, customFileName, customExtension, testClass);
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
        final ApprovalFiles files = approvedAndReceivedPaths();
        writeReceivedFile(output, files);
        verifyImpl(files);
    }


    /**
     * Compares the actual output of your program (the function's argument) and the content of the *approved* file
     * matching with the test method.
     *
     * It'll use a temporary *received* folder to store a copy of the output of your program. This file will be erased
     * in case the results are matching. Otherwise, it will be kept for you to review it.
     *
     * In case of differences found in the output, the {@link Reporter} linked to this `Approvals` instance will be
     * called ({@link Reporter#mismatch(Path, Path)}).
     *
     * @param output a {@link Path} containing the output of your program. It will be compared to the associated
     *               *approved* file.
     * @throws AssertionError   if the {@link Reporter} implementation relies on standard assertions provided by a
     *                          framework like JUnit
     * @throws RuntimeException if the {@link Reporter} relies on executing an external command which failed
     */
    public void verify(final Path output) {
        final ApprovalFiles files = approvedAndReceivedPaths().resolve(output);
        copy(output, files.received);
        verifyImpl(files);
    }

    private void verifyImpl(final ApprovalFiles files) {
        createFileIfNeeded(files.approved);
        if (files.haveSameContent()) {
            silentRemove(files.received);
        } else {
            reporter.mismatch(files.approved, files.received);
            new ThrowsReporter().mismatch(files.approved, files.received);
        }
    }

    //Can be overridden to add a header to the file
    void writeReceivedFile(final Object output, final ApprovalFiles files) {
        write(output + "", files.received);
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
    private static Path folderForClass(final Class<?> testClass) {
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

    ApprovalFiles approvedAndReceivedPaths() {
        return build(
            folder,
            customFileName != null ? customFileName : callerMethodName(),
            customExtension);
    }

}
