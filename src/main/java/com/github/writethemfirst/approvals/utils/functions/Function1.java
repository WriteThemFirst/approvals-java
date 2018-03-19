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
 * # Function1
 *
 * Represents a function with one argument.
 *
 * @param <IN1> Type of the argument 1 of the function
 * @param <OUT> Return type of the function
 */
@FunctionalInterface
public interface Function1<IN1, OUT> {
    /**
     * Applies the function to the provided argument and returns the result.
     *
     * @param arg1 Argument 1
     * @return The result of function application
     */
    OUT apply(final IN1 arg1);
}
