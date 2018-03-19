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
package com.github.writethemfirst.approvals.utils.functions;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

public class FunctionUtils {
    private static final Object EMPTY_ENTRY = new Object();
    private static final Iterable<Object> EMPTY = Collections.singletonList(EMPTY_ENTRY);

    public static <I1> String callWithAllCombinations(
        final Iterable<I1> args1,
        final Function1<I1, ?> f) {

        return callWithAllCombinations(args1, EMPTY, (e1, e2) -> f.apply(e1));
    }

    public static <I1, I2> String callWithAllCombinations(
        final Iterable<I1> args1,
        final Iterable<I2> args2,
        final Function2<I1, I2, ?> f) {

        return callWithAllCombinations(args1, args2, EMPTY, (e1, e2, e3) -> f.apply(e1, e2));
    }

    public static <I1, I2, I3> String callWithAllCombinations(
        final Iterable<I1> args1,
        final Iterable<I2> args2,
        final Iterable<I3> args3,
        final Function3<I1, I2, I3, ?> f) {

        return callWithAllCombinations(args1, args2, args3, EMPTY, (e1, e2, e3, e4) -> f.call(e1, e2, e3));

    }

    public static <I1, I2, I3, I4> String callWithAllCombinations(
        final Iterable<I1> args1,
        final Iterable<I2> args2,
        final Iterable<I3> args3,
        final Iterable<I4> args4,
        final Function4<I1, I2, I3, I4, ?> f) {

        return callWithAllCombinations(args1, args2, args3, args4, EMPTY, (e1, e2, e3, e4, e5) -> f.call(e1, e2, e3, e4));
    }

    public static <I1, I2, I3, I4, I5> String callWithAllCombinations(
        final Iterable<I1> args1,
        final Iterable<I2> args2,
        final Iterable<I3> args3,
        final Iterable<I4> args4,
        final Iterable<I5> args5,
        final Function5<I1, I2, I3, I4, I5, ?> f) {
        final StringBuilder results = new StringBuilder();
        for (final I1 a1 : args1) {
            for (final I2 a2 : args2) {
                for (final I3 a3 : args3) {
                    for (final I4 a4 : args4) {
                        for (final I5 a5 : args5) {
                            try {
                                results.append(f.call(a1, a2, a3, a4, a5));
                            } catch (final Exception e) {
                                results.append(e);
                            }
                            results.append(mkString(
                                " <== , ",
                                ", ",
                                "\n",
                                a1, a2, a3, a4, a5));
                        }
                    }
                }
            }
        }
        return results.toString();
    }

    private static String mkString(
        final String prefix,
        final String delimiter,
        final String suffix,
        final Object... objects) {

        return Arrays.stream(objects)
            .filter(o -> o != EMPTY_ENTRY)
            .map(Object::toString)
            .collect(Collectors.joining(delimiter, prefix, suffix));
    }
}
