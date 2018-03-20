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
package com.github.writethemfirst.approvals.utils;

import com.github.writethemfirst.approvals.utils.functions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * # FunctionUtils
 *
 * Set of static methods allowing to ease the manipulation of functions applications.
 *
 * This particular class aims at allowing the application of functions on combinations of parameter values by looping
 * over all values and producing the result. Since the result is to be used for *Approval Testing*, the applied
 * functions will return String values.
 */
public class FunctionUtils {

    /**
     * Simple marker to distinguish arguments which are not specified while calling the function.
     *
     * Using a dummy object allows to filter the valid arguments from the non valid ones.
     */
    private static final Object UNSPECIFIED_ENTRY = new Object();

    /**
     * Simple wrapper for unspecified arguments. This list actually only contains a dummy object allowing to filter the
     * valid arguments from the non valid ones.
     */
    private static final Iterable<Object> UNSPECIFIED = Collections.singletonList(UNSPECIFIED_ENTRY);

    /**
     * Applies the provided function to all combinations of all the provided arguments for the function, and returns the
     * produced results as a String.
     *
     * The produced String will contain both the provided arguments and the computed value, and can later be used for
     * writting the *approvals files*.
     *
     * @param args1    A collection of all the arguments 1 to apply the function to
     * @param function The function to apply on the combinations of provided arguments
     * @param <IN1>    Type of the argument 1
     * @return A String containing all the combinations of provided arguments, and the result produced by the function
     * application
     */
    public static <IN1> String applyCombinations(final Iterable<IN1> args1, final Function1<IN1, ?> function) {
        return applyCombinations(args1, UNSPECIFIED, (arg1, arg2) -> function.apply(arg1));
    }

    /**
     * Applies the provided function to all combinations of all the provided arguments for the function, and returns the
     * produced results as a String.
     *
     * The produced String will contain both the provided arguments and the computed value, and can later be used for
     * writting the *approvals files*.
     *
     * @param args1    A collection of all the arguments 1 to apply the function to
     * @param args2    A collection of all the arguments 2 to apply the function to
     * @param function The function to apply on the combinations of provided arguments
     * @param <IN1>    Type of the argument 1
     * @param <IN2>    Type of the argument 2
     * @return A String containing all the combinations of provided arguments, and the result produced by the function
     * application
     */
    public static <IN1, IN2> String applyCombinations(final Iterable<IN1> args1, final Iterable<IN2> args2,
                                                      final Function2<IN1, IN2, ?> function) {
        return applyCombinations(args1, args2, UNSPECIFIED, (arg1, arg2, arg3) -> function.apply(arg1, arg2));
    }

    /**
     * Applies the provided function to all combinations of all the provided arguments for the function, and returns the
     * produced results as a String.
     *
     * The produced String will contain both the provided arguments and the computed value, and can later be used for
     * writting the *approvals files*.
     *
     * @param args1    A collection of all the arguments 1 to apply the function to
     * @param args2    A collection of all the arguments 2 to apply the function to
     * @param args3    A collection of all the arguments 3 to apply the function to
     * @param function The function to apply on the combinations of provided arguments
     * @param <IN1>    Type of the argument 1
     * @param <IN2>    Type of the argument 2
     * @param <IN3>    Type of the argument 3
     * @return A String containing all the combinations of provided arguments, and the result produced by the function
     * application
     */
    public static <IN1, IN2, IN3> String applyCombinations
    (final Iterable<IN1> args1, final Iterable<IN2> args2, final Iterable<IN3> args3,
     final Function3<IN1, IN2, IN3, ?> function) {
        return applyCombinations(args1, args2, args3, UNSPECIFIED,
            (arg1, arg2, arg3, arg4) -> function.apply(arg1, arg2, arg3));
    }

    /**
     * Applies the provided function to all combinations of all the provided arguments for the function, and returns the
     * produced results as a String.
     *
     * The produced String will contain both the provided arguments and the computed value, and can later be used for
     * writting the *approvals files*.
     *
     * @param args1    A collection of all the arguments 1 to apply the function to
     * @param args2    A collection of all the arguments 2 to apply the function to
     * @param args3    A collection of all the arguments 3 to apply the function to
     * @param args4    A collection of all the arguments 4 to apply the function to
     * @param function The function to apply on the combinations of provided arguments
     * @param <IN1>    Type of the argument 1
     * @param <IN2>    Type of the argument 2
     * @param <IN3>    Type of the argument 3
     * @param <IN4>    Type of the argument 4
     * @return A String containing all the combinations of provided arguments, and the result produced by the function
     * application
     */
    public static <IN1, IN2, IN3, IN4> String applyCombinations
    (final Iterable<IN1> args1, final Iterable<IN2> args2, final Iterable<IN3> args3, final Iterable<IN4> args4,
     final Function4<IN1, IN2, IN3, IN4, ?> function) {
        return applyCombinations(args1, args2, args3, args4, UNSPECIFIED,
            (arg1, arg2, arg3, arg4, arg5) -> function.apply(arg1, arg2, arg3, arg4));
    }

    /**
     * Applies the provided function to all combinations of all the provided arguments for the function, and returns the
     * produced results as a String.
     *
     * The produced String will contain both the provided arguments and the computed value, and can later be used for
     * writting the *approvals files*.
     *
     * @param args1    A collection of all the arguments 1 to apply the function to
     * @param args2    A collection of all the arguments 2 to apply the function to
     * @param args3    A collection of all the arguments 3 to apply the function to
     * @param args4    A collection of all the arguments 4 to apply the function to
     * @param args5    A collection of all the arguments 5 to apply the function to
     * @param function The function to apply on the combinations of provided arguments
     * @param <IN1>    Type of the argument 1
     * @param <IN2>    Type of the argument 2
     * @param <IN3>    Type of the argument 3
     * @param <IN4>    Type of the argument 4
     * @param <IN5>    Type of the argument 5
     * @return A String containing all the combinations of provided arguments, and the result produced by the function
     * application
     */
    public static <IN1, IN2, IN3, IN4, IN5> String applyCombinations
    (final Iterable<IN1> args1, final Iterable<IN2> args2, final Iterable<IN3> args3, final Iterable<IN4> args4,
     final Iterable<IN5> args5, final Function5<IN1, IN2, IN3, IN4, IN5, ?> function) {
        final StringBuilder results = new StringBuilder();
        for (final IN1 arg1 : args1) {
            for (final IN2 arg2 : args2) {
                for (final IN3 arg3 : args3) {
                    for (final IN4 arg4 : args4) {
                        for (final IN5 arg5 : args5) {
                            try {
                                results.append(function.apply(arg1, arg2, arg3, arg4, arg5));
                            } catch (final Exception e) {
                                results.append(e);
                            }
                            results.append(mkString(" <== , ", ", ", "\n",
                                arg1, arg2, arg3, arg4, arg5));
                        }
                    }
                }
            }
        }
        return results.toString();
    }

    private static String mkString(final String prefix, final String delimiter, final String suffix,
                                   final Object... objects) {
        return Arrays.stream(objects)
            .filter(o -> o != UNSPECIFIED_ENTRY)
            .map(Object::toString)
            .collect(Collectors.joining(delimiter, prefix, suffix));
    }
}
