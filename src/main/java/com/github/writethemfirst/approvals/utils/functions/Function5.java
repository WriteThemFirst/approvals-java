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

/**
 * # Function5
 *
 * Represents a function with five arguments.
 *
 * @param <IN1> Type of the argument 1 of the function
 * @param <IN2> Type of the argument 2 of the function
 * @param <IN3> Type of the argument 3 of the function
 * @param <IN4> Type of the argument 4 of the function
 * @param <IN5> Type of the argument 5 of the function
 * @param <OUT> Return type of the function
 */
@FunctionalInterface
public interface Function5<IN1, IN2, IN3, IN4, IN5, OUT> {
    /**
     * Applies the function to the provided arguments and returns the result.
     *
     * @param arg1 Argument 1
     * @param arg2 Argument 2
     * @param arg3 Argument 3
     * @param arg4 Argument 4
     * @param arg5 Argument 5
     * @return The result of function application
     */
    OUT apply(final IN1 arg1, final IN2 arg2, final IN3 arg3, final IN4 arg4, final IN5 arg5);
}
