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
import com.github.writethemfirst.approvals.utils.FileUtils;
import com.github.writethemfirst.approvals.utils.functions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.github.writethemfirst.approvals.files.ApprovedAndReceivedPaths.approvedAndReceived;
import static com.github.writethemfirst.approvals.utils.FileUtils.*;
import static com.github.writethemfirst.approvals.utils.StackUtils.callerClass;
import static com.github.writethemfirst.approvals.utils.StackUtils.callerMethod;
import static com.github.writethemfirst.approvals.utils.functions.FunctionUtils.callWithAllCombinations;
import static java.nio.file.Paths.get;
import static java.util.Arrays.stream;
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
 * @see Reporter
 */
public class Approvals {

    /**
     * The Reporter to be used to report any mismatches while computing the files comparisons.
     */
    private final Reporter reporter;
    private final String customFileName;
    private final Class<?> testClass;
    private final Path folder;
    private final String customExtension;
    private final String header;


    public Approvals() {
        this(Reporter.DEFAULT,
            null,
            callerClass(Approvals.class),
            folderForClass(callerClass(Approvals.class)),
            "",
            "");
    }

    private Approvals(
        Reporter reporter,
        String customFileName,
        Class<?> testClass,
        Path folder,
        String customExtension,
        String header) {

        this.reporter = reporter;
        this.customFileName = customFileName;
        this.testClass = testClass;
        this.folder = folder;
        this.customExtension = customExtension;
        this.header = header;
    }

    public Approvals reportTo(final Reporter reporter) {
        return new Approvals(reporter, customFileName, testClass, folder, customExtension, header);
    }

    public Approvals writeTo(final String customFileName) {
        return new Approvals(reporter, customFileName, testClass, folder, customExtension, header);
    }

    public Approvals header(final String headerWithLineFeed) {
        return new Approvals(reporter, customFileName, testClass, folder, customExtension, headerWithLineFeed);
    }

    public Approvals namedArguments(final String... names) {
        return header(stream(names).collect(Collectors.joining(
            ", ",
            "result, ",
            "\n"
        )));
    }

    private Approvals extension(final String extensionWithDot) {
        return new Approvals(reporter, customFileName, testClass, folder, extensionWithDot, header);
    }

    private Approvals csv() {
        return extension(".csv");
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
        write(header + output, files.received);
        silentCreateFile(files.approved);
        if (files.filesHaveSameContent()) {
            silentRemove(files.received);
        } else {
            reporter.mismatch(files.approved, files.received);
            new ThrowsReporter().mismatch(files.approved, files.received);
        }
    }


    /**
     * Compares the actual output of your program (files in the folder `actualFolder`) and the content of the *approved*
     * "Master" folder matching with the test method.
     *
     * It'll use a temporary *received* folder to copy the actual files from your program. This folder and its files
     * will be erased in case the results are matching. Otherwise, they will be kept for you to review it.
     *
     * In case of differences found in the output, the {@link Reporter} linked to this `Approvals` instance will be
     * called ({@link Reporter#mismatch(Path, Path)}) for each mismatched file.
     *
     * @param actualFolder the folder containing the output of your program. It will be compared to the associated
     *                     *approved* folder
     * @throws AssertionError   if the {@link Reporter} implementation relies on standard assertions provided by a
     *                          framework like JUnit
     * @throws RuntimeException if the {@link Reporter} relies on executing an external command which failed
     */
    public void verifyAllFiles(final Path actualFolder) {
        final ApprovedAndReceivedPaths approvedAndReceivedPaths = approvedAndReceivedPaths();
        prepareFolders(actualFolder, approvedAndReceivedPaths);
        final Map<Boolean, List<ApprovedAndReceivedPaths>> matchesAndMismatches =
            approvedAndReceivedPaths
                .allFilesToCheck()
                .collect(partitioningBy(ApprovedAndReceivedPaths::filesHaveSameContent));

        cleanupReceivedFiles(approvedAndReceivedPaths, matchesAndMismatches);
        reportMismatches(matchesAndMismatches);
    }

    private void reportMismatches(final Map<Boolean, List<ApprovedAndReceivedPaths>> matchesAndMismatches) {
        matchesAndMismatches.get(false).forEach(mismatch -> reporter.mismatch(mismatch.approved, mismatch.received));
    }

    private void cleanupReceivedFiles(final ApprovedAndReceivedPaths approvedAndReceivedPaths, final Map<Boolean, List<ApprovedAndReceivedPaths>> matchesAndMismatches) {
        matchesAndMismatches.get(true).forEach(ar -> silentRemove(ar.received));
        if (matchesAndMismatches.get(false).isEmpty()) {
            silentRecursiveRemove(approvedAndReceivedPaths.received);
        }
    }

    /**
     * Copies files from *actual* to *received* folder, and creates missing *approved* files.
     */
    private void prepareFolders(final Path actualFolder, final ApprovedAndReceivedPaths approvedAndReceivedPaths) {
        try {
            Files.createDirectories(approvedAndReceivedPaths.approved);
        } catch (IOException e) {
            throw new RuntimeException("could not create *approved* folder " + approvedAndReceivedPaths.approved, e);
        }
        searchFiles(actualFolder).forEach(p -> FileUtils.copyToFolder(p, approvedAndReceivedPaths.received));
        approvedAndReceivedPaths
            .allFilesToCheck()
            .map(paths -> paths.approved)
            .forEach(FileUtils::silentCreateFile);
    }

    /**
     * Compares the actual output of your program and the content of the *approved* file matching with the test method
     * (see {@link #verify(Object)} for details).
     *
     * The lambda or method `f` is called for all values of `args1` specified in the {@link Iterable}. Then the results
     * are stored, one line for each value, in a String, which is used as in the standard {@link #verify(Object)}
     * method.
     *
     * @param args1 all values for the argument of `f` you want to verify
     * @param f     the lambda or method you want to test
     * @throws AssertionError   if the {@link Reporter} implementation relies on standard assertions provided by a
     *                          framework like JUnit
     * @throws RuntimeException if the {@link Reporter} relies on executing an external command which failed
     */
    public <I1> void verifyAllCombinations(final Iterable<I1> args1, final Function1<I1, ?> f) {
        csv().verify(callWithAllCombinations(args1, f));
    }

    /**
     * Compares the actual output of your program and the content of the *approved* file matching with the test method
     * (see {@link #verify(Object)} for details).
     *
     * The lambda or method `f` is called for all values of `args1` specified in the array. Then the results are stored,
     * one line for each value, in a String, which is used as in the standard {@link #verify(Object)} method.
     *
     * @param args1 all values for the argument of `f` you want to verify
     * @param f     the lambda or method you want to test
     * @throws AssertionError   if the {@link Reporter} implementation relies on standard assertions provided by a
     *                          framework like JUnit
     * @throws RuntimeException if the {@link Reporter} relies on executing an external command which failed
     */
    public <I1> void verifyAllCombinations(final I1[] args1, final Function1<I1, ?> f) {
        verifyAllCombinations(Arrays.asList(args1), f);
    }

    /**
     * Compares the actual output of your program and the content of the *approved* file matching with the test method
     * (see {@link #verify(Object)} for details).
     *
     * The lambda or method `f` is called for all values of `args1` and `args2` specified in the {@link Iterable}s. Then
     * the results are stored, one line for each combination, in a String, which is used as in the standard {@link
     * #verify(Object)} method.
     *
     * @param args1 all values for the first argument of `f` you want to verify
     * @param args2 all values for the second argument of `f` you want to verify
     * @param f     the lambda or method you want to test
     * @throws AssertionError   if the {@link Reporter} implementation relies on standard assertions provided by a
     *                          framework like JUnit
     * @throws RuntimeException if the {@link Reporter} relies on executing an external command which failed
     */
    public <I1, I2> void verifyAllCombinations(
        final Iterable<I1> args1,
        final Iterable<I2> args2,
        final Function2<I1, I2, ?> f) {

        csv().verify(callWithAllCombinations(args1, args2, f));
    }

    /**
     * Compares the actual output of your program and the content of the *approved* file matching with the test method
     * (see {@link #verify(Object)} for details).
     *
     * The lambda or method `f` is called for all values of `args1` and `args2` specified in the arrayss. Then the
     * results are stored, one line for each combination, in a String, which is used as in the standard {@link
     * #verify(Object)} method.
     *
     * @param args1 all values for the first argument of `f` you want to verify
     * @param args2 all values for the second argument of `f` you want to verify
     * @param f     the lambda or method you want to test
     * @throws AssertionError   if the {@link Reporter} implementation relies on standard assertions provided by a
     *                          framework like JUnit
     * @throws RuntimeException if the {@link Reporter} relies on executing an external command which failed
     */
    public <I1, I2> void verifyAllCombinations(
        final I1[] args1,
        final I2[] args2,
        final Function2<I1, I2, ?> f) {

        verifyAllCombinations(
            Arrays.asList(args1),
            Arrays.asList(args2),
            f);
    }

    /**
     * Compares the actual output of your program and the content of the *approved* file matching with the test method
     * (see {@link #verify(Object)} for details).
     *
     * The lambda or method `f` is called for all values of `args1`, `args2` and `args3` specified in the {@link
     * Iterable}s. Then the results are stored, one line for each combination, in a String, which is used as in the
     * standard {@link #verify(Object)} method.
     *
     * @param args1 all values for the first argument of `f` you want to verify
     * @param args2 all values for the second argument of `f` you want to verify
     * @param args3 all values for the third argument of `f` you want to verify
     * @param f     the lambda or method you want to test
     * @throws AssertionError   if the {@link Reporter} implementation relies on standard assertions provided by a
     *                          framework like JUnit
     * @throws RuntimeException if the {@link Reporter} relies on executing an external command which failed
     */
    public <I1, I2, I3> void verifyAllCombinations(
        final Iterable<I1> args1,
        final Iterable<I2> args2,
        final Iterable<I3> args3,
        final Function3<I1, I2, I3, ?> f) {

        csv().verify(callWithAllCombinations(args1, args2, args3, f));
    }

    /**
     * Compares the actual output of your program and the content of the *approved* file matching with the test method
     * (see {@link #verify(Object)} for details).
     *
     * The lambda or method `f` is called for all values of `args1`, `args2` and `args3` specified in the arrays. Then
     * the results are stored, one line for each combination, in a String, which is used as in the standard {@link
     * #verify(Object)} method.
     *
     * @param args1 all values for the first argument of `f` you want to verify
     * @param args2 all values for the second argument of `f` you want to verify
     * @param args3 all values for the third argument of `f` you want to verify
     * @param f     the lambda or method you want to test
     * @throws AssertionError   if the {@link Reporter} implementation relies on standard assertions provided by a
     *                          framework like JUnit
     * @throws RuntimeException if the {@link Reporter} relies on executing an external command which failed
     */
    public <I1, I2, I3> void verifyAllCombinations(
        final I1[] args1,
        final I2[] args2,
        final I3[] args3,
        final Function3<I1, I2, I3, ?> f) {

        verifyAllCombinations(
            Arrays.asList(args1),
            Arrays.asList(args2),
            Arrays.asList(args3),
            f);
    }

    /**
     * Compares the actual output of your program and the content of the *approved* file matching with the test method
     * (see {@link #verify(Object)} for details).
     *
     * The lambda or method `f` is called for all values of `args1`, `args2`, `args3` and `args4` specified in the
     * {@link Iterable}s. Then the results are stored, one line for each combination, in a String, which is used as in
     * the standard {@link #verify(Object)} method.
     *
     * @param args1 all values for the first argument of `f` you want to verify
     * @param args2 all values for the second argument of `f` you want to verify
     * @param args3 all values for the third argument of `f` you want to verify
     * @param args4 all values for the fourth argument of `f` you want to verify
     * @param f     the lambda or method you want to test
     * @throws AssertionError   if the {@link Reporter} implementation relies on standard assertions provided by a
     *                          framework like JUnit
     * @throws RuntimeException if the {@link Reporter} relies on executing an external command which failed
     */
    public <I1, I2, I3, I4> void verifyAllCombinations(
        final Iterable<I1> args1,
        final Iterable<I2> args2,
        final Iterable<I3> args3,
        final Iterable<I4> args4,
        final Function4<I1, I2, I3, I4, ?> f) {

        csv().verify(callWithAllCombinations(args1, args2, args3, args4, f));
    }

    /**
     * Compares the actual output of your program and the content of the *approved* file matching with the test method
     * (see {@link #verify(Object)} for details).
     *
     * The lambda or method `f` is called for all values of `args1`, `args2`, `args3` and `args4` specified in the
     * arrays. Then the results are stored, one line for each combination, in a String, which is used as in the standard
     * {@link #verify(Object)} method.
     *
     * @param args1 all values for the first argument of `f` you want to verify
     * @param args2 all values for the second argument of `f` you want to verify
     * @param args3 all values for the third argument of `f` you want to verify
     * @param args4 all values for the fourth argument of `f` you want to verify
     * @param f     the lambda or method you want to test
     * @throws AssertionError   if the {@link Reporter} implementation relies on standard assertions provided by a
     *                          framework like JUnit
     * @throws RuntimeException if the {@link Reporter} relies on executing an external command which failed
     */
    public <I1, I2, I3, I4> void verifyAllCombinations(
        final I1[] args1,
        final I2[] args2,
        final I3[] args3,
        final I4[] args4,
        final Function4<I1, I2, I3, I4, ?> f) {
        verifyAllCombinations(
            Arrays.asList(args1),
            Arrays.asList(args2),
            Arrays.asList(args3),
            Arrays.asList(args4),
            f);
    }

    /**
     * Compares the actual output of your program and the content of the *approved* file matching with the test method
     * (see {@link #verify(Object)} for details).
     *
     * The lambda or method `f` is called for all values of `args1`, `args2`, `args3`, `args4` and `args5` specified in
     * the {@link Iterable}s. Then the results are stored, one line for each combination, in a String, which is used as
     * in the standard {@link #verify(Object)} method.
     *
     * @param args1 all values for the first argument of `f` you want to verify
     * @param args2 all values for the second argument of `f` you want to verify
     * @param args3 all values for the third argument of `f` you want to verify
     * @param args4 all values for the fourth argument of `f` you want to verify
     * @param args5 all values for the fifth argument of `f` you want to verify
     * @param f     the lambda or method you want to test
     * @throws AssertionError   if the {@link Reporter} implementation relies on standard assertions provided by a
     *                          framework like JUnit
     * @throws RuntimeException if the {@link Reporter} relies on executing an external command which failed
     */
    public <I1, I2, I3, I4, I5> void verifyAllCombinations(
        final Iterable<I1> args1,
        final Iterable<I2> args2,
        final Iterable<I3> args3,
        final Iterable<I4> args4,
        final Iterable<I5> args5,
        final Function5<I1, I2, I3, I4, I5, ?> f) {

        csv().verify(callWithAllCombinations(args1, args2, args3, args4, args5, f));
    }

    /**
     * Compares the actual output of your program and the content of the *approved* file matching with the test method
     * (see {@link #verify(Object)} for details).
     *
     * The lambda or method `f` is called for all values of `args1`, `args2`, `args3`, `args4` and `args5` specified in
     * the arrays. Then the results are stored, one line for each combination, in a String, which is used as in the
     * standard {@link #verify(Object)} method.
     *
     * @param args1 all values for the first argument of `f` you want to verify
     * @param args2 all values for the second argument of `f` you want to verify
     * @param args3 all values for the third argument of `f` you want to verify
     * @param args4 all values for the fourth argument of `f` you want to verify
     * @param args5 all values for the fifth argument of `f` you want to verify
     * @param f     the lambda or method you want to test
     * @throws AssertionError   if the {@link Reporter} implementation relies on standard assertions provided by a
     *                          framework like JUnit
     * @throws RuntimeException if the {@link Reporter} relies on executing an external command which failed
     */
    public <I1, I2, I3, I4, I5> void verifyAllCombinations(
        final I1[] args1,
        final I2[] args2,
        final I3[] args3,
        final I4[] args4,
        final I5[] args5,
        final Function5<I1, I2, I3, I4, I5, ?> f) {
        verifyAllCombinations(
            Arrays.asList(args1),
            Arrays.asList(args2),
            Arrays.asList(args3),
            Arrays.asList(args4),
            Arrays.asList(args5),
            f);
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

    private ApprovedAndReceivedPaths approvedAndReceivedPaths() {
        return approvedAndReceived(
            folder,
            customFileName != null ? customFileName : callerMethodName(),
            customExtension);
    }

}
