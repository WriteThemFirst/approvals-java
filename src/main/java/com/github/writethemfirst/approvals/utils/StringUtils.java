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

import java.util.Arrays;
import java.util.List;

import static java.util.stream.IntStream.range;

public class StringUtils {
    public static boolean sameContent(String expected, String actual) {
        final List<String> expectedElements = splitOnLineEndings(expected);
        final List<String> actualElements = splitOnLineEndings(actual);
        return expectedElements.size() == actualElements.size() &&
            range(0, expectedElements.size()).allMatch(i -> expectedElements.get(i).equals(actualElements.get(i)));
    }

    public static List<String> splitOnLineEndings(final String s) {
        return Arrays.asList(s.split("\r\n|\n"));
    }
}
