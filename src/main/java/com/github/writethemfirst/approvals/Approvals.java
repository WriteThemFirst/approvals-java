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

import com.github.writethemfirst.approvals.approvers.Approver;
import com.github.writethemfirst.approvals.approvers.CombinationApprover;
import com.github.writethemfirst.approvals.utils.functions.*;

import java.nio.file.Path;

public class Approvals {
    private Approvals() {
        // not meant to be instanciated, use static methods
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
    public static void verify(final Object output) {
        new Approver().verify(output);
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
    public static void verify(final Path output) {
        new Approver().verify(output);
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
    public static void verifyAllFiles(final Path actualFolder) {
        new Approver().verify(actualFolder);
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
    public static <I1> void verifyAllCombinations(final Iterable<I1> args1, final Function1<I1, ?> f) {
        new CombinationApprover().verifyAllCombinations(args1, f);
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
    public static <I1> void verifyAllCombinations(final I1[] args1, final Function1<I1, ?> f) {
        new CombinationApprover().verifyAllCombinations(args1, f);
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
    public static <I1, I2> void verifyAllCombinations(
        final Iterable<I1> args1,
        final Iterable<I2> args2,
        final Function2<I1, I2, ?> f) {

        new CombinationApprover().verifyAllCombinations(args1, args2, f);
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
    public static <I1, I2> void verifyAllCombinations(
        final I1[] args1,
        final I2[] args2,
        final Function2<I1, I2, ?> f) {

        new CombinationApprover().verifyAllCombinations(args1, args2, f);
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
    public static <I1, I2, I3> void verifyAllCombinations(
        final Iterable<I1> args1,
        final Iterable<I2> args2,
        final Iterable<I3> args3,
        final Function3<I1, I2, I3, ?> f) {

        new CombinationApprover().verifyAllCombinations(args1, args2, args3, f);
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
    public static <I1, I2, I3> void verifyAllCombinations(
        final I1[] args1,
        final I2[] args2,
        final I3[] args3,
        final Function3<I1, I2, I3, ?> f) {

        new CombinationApprover().verifyAllCombinations(args1, args2, args3, f);
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
    public static <I1, I2, I3, I4> void verifyAllCombinations(
        final Iterable<I1> args1,
        final Iterable<I2> args2,
        final Iterable<I3> args3,
        final Iterable<I4> args4,
        final Function4<I1, I2, I3, I4, ?> f) {

        new CombinationApprover().verifyAllCombinations(args1, args2, args3, args4, f);
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
    public static <I1, I2, I3, I4> void verifyAllCombinations(
        final I1[] args1,
        final I2[] args2,
        final I3[] args3,
        final I4[] args4,
        final Function4<I1, I2, I3, I4, ?> f) {

        new CombinationApprover().verifyAllCombinations(args1, args2, args3, args4, f);
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
    public static <I1, I2, I3, I4, I5> void verifyAllCombinations(
        final Iterable<I1> args1,
        final Iterable<I2> args2,
        final Iterable<I3> args3,
        final Iterable<I4> args4,
        final Iterable<I5> args5,
        final Function5<I1, I2, I3, I4, I5, ?> f) {

        new CombinationApprover().verifyAllCombinations(args1, args2, args3, args4, args5, f);
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
    public static <I1, I2, I3, I4, I5> void verifyAllCombinations(
        final I1[] args1,
        final I2[] args2,
        final I3[] args3,
        final I4[] args4,
        final I5[] args5,
        final Function5<I1, I2, I3, I4, I5, ?> f) {

        new CombinationApprover().verifyAllCombinations(args1, args2, args3, args4, args5, f);
    }

}
