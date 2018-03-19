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

import com.github.writethemfirst.approvals.files.ApprovedAndReceivedPaths;
import com.github.writethemfirst.approvals.reporters.ThrowsReporter;

import java.nio.file.Path;

import static com.github.writethemfirst.approvals.files.ApprovedAndReceivedPaths.approvedAndReceived;
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
 * Approvals#verify(Object)} method, allowing to compute a comparison between an output and some content stored in an
 * *approved* file.
 *
 * @see Reporter
 */
public class Approvals {
    private final Class<?> testClass = callerClass(getClass());
    private final Path folder = folderForClass(testClass);
    final Reporter reporter;
    final String customFileName;
    final String customExtension;

    /**
     * Standard approvals, with default {@link Reporter} and file name detected from the test class used to apply the
     * constructor and the test method used to apply a {@link #verify(Object)} method.
     */
    public Approvals() {
        this(Reporter.DEFAULT, null, "");
    }

    /**
     * Protected constructor used by the "copy" methods.
     */
    Approvals(
        final Reporter reporter,
        final String customFileName,
        final String customExtension) {

        this.reporter = reporter;
        this.customFileName = customFileName;
        this.customExtension = customExtension;
    }

    /**
     * Specifies the reporter used to report mismatches.
     *
     * @return a copy of this Approvals
     */
    public Approvals reportTo(final Reporter reporter) {
        return new Approvals(reporter, customFileName, customExtension);
    }

    /**
     * Specifies the name to use for *approved* and *received* files.
     *
     * @return a copy of this Approvals
     */
    public Approvals writeTo(final String customFileName) {
        return new Approvals(reporter, customFileName, customExtension);
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
        final ApprovedAndReceivedPaths files = approvedAndReceivedPaths();
        writeReceivedFile(output, files);
        createFile(files.approved);
        if (files.filesHaveSameContent()) {
            silentRemove(files.received);
        } else {
            reporter.mismatch(files.approved, files.received);
            new ThrowsReporter().mismatch(files.approved, files.received);
        }
    }

    //Can be overridden to add a header to the file.
    void writeReceivedFile(final Object output, final ApprovedAndReceivedPaths files) {
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

    ApprovedAndReceivedPaths approvedAndReceivedPaths() {
        return approvedAndReceived(
            folder,
            customFileName != null ? customFileName : callerMethodName(),
            customExtension);
    }

}
