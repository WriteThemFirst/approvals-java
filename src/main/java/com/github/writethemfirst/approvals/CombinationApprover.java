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

import com.github.writethemfirst.approvals.files.ApprovalFiles;
import com.github.writethemfirst.approvals.utils.FunctionUtils;
import com.github.writethemfirst.approvals.utils.functions.*;

import java.util.Arrays;
import java.util.stream.Collectors;

import static com.github.writethemfirst.approvals.utils.FileUtils.write;
import static com.github.writethemfirst.approvals.utils.FunctionUtils.applyCombinations;
import static java.util.Arrays.stream;

/**
 * Approves a function by calling it with combinations of parameters. See {@link #verifyAllCombinations(Object[],
 * Object[], Object[], Function3)}.
 */
public class CombinationApprover extends Approver {
    private final String header;

    public CombinationApprover() {
        super();
        header = "";
    }

    /**
     * Specifies the reporter used to report mismatches.
     *
     * @return a copy of this CombinationApprovals
     */
    @Override
    public CombinationApprover reportTo(final Reporter reporter) {
        return new CombinationApprover(reporter, customFileName, customExtension, header);
    }

    /**
     * Specifies the name to use for *approved* and *received* files.
     *
     * @return a copy of this CombinationApprovals
     */
    @Override
    public CombinationApprover writeTo(final String customFileName) {
        return new CombinationApprover(reporter, customFileName, customExtension, header);
    }

    /**
     * Specifies a header naming the arguments of the function under test. This header prefixes the *approved* and
     * *received* CSV files.
     *
     * @param names one name for each argument
     * @return a copy of the {@link CombinationApprover}
     */
    public CombinationApprover namedArguments(final String... names) {
        return header(stream(names).collect(Collectors.joining(
            ", ",
            "result, ",
            "\n"
        )));
    }

    private CombinationApprover(
        final Reporter reporter,
        final String customFileName,
        final String customExtension,
        final String headerWithLineFeed) {

        super(reporter, customFileName, customExtension);
        this.header = headerWithLineFeed;
    }


    private CombinationApprover csv() {
        return new CombinationApprover(reporter, customFileName, ".csv", header);
    }


    private CombinationApprover header(final String headerWithLineFeed) {
        return new CombinationApprover(reporter, customFileName, customExtension, headerWithLineFeed);
    }


    @Override
    void writeReceivedFile(final Object output, final ApprovalFiles files) {
        write(header + output, files.received);
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
        csv().verify(FunctionUtils.applyCombinations(args1, f));
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

        csv().verify(FunctionUtils.applyCombinations(args1, args2, f));
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

        csv().verify(FunctionUtils.applyCombinations(args1, args2, args3, f));
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

        csv().verify(FunctionUtils.applyCombinations(args1, args2, args3, args4, f));
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

        csv().verify(applyCombinations(args1, args2, args3, args4, args5, f));
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


}
