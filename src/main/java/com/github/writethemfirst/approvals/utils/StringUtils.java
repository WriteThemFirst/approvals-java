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

import static java.lang.String.format;
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

    public static String describeDifferences(final String expected, final String actual) {
        final List<String> expectedList = splitOnLineEndings(expected);
        final List<String> actualList = splitOnLineEndings(actual);
        final int expectedSize = expectedList.size();
        final int actualSize = actualList.size();
        if (expectedSize != actualSize) {
            return format("expected:%n%s%n(%d lines) but was:%n%s%n(%d lines)", expected, expectedSize, actual, actualSize);
        } else if (expectedSize == 1) {
            return describeDifferentLines(expected, actual, 0);
        } else {
            final int lineIndex = firstDifferentLine(expectedList, actualList);
            return format("expected:%n%s%n but was:%n%s%n", expected, actual) +
                describeDifferentLines(expectedList.get(lineIndex), actualList.get(lineIndex), lineIndex);
        }
    }

    private static String describeDifferentLines(final String expected, final String actual, final Integer lineIndex) {
        final String prefix = greatestCommonPrefix(expected, actual);
        final String suffix = greatestCommonSuffix(expected.substring(prefix.length()), actual.substring(prefix.length()));
        final String expectedCenter = expected.substring(prefix.length(), expected.length() - suffix.length());
        final String actualCenter = actual.substring(prefix.length(), actual.length() - suffix.length());
        return format("first difference at line#%d col#%d: expected %s[%s]%s but was %s[%s]%s",
            lineIndex, prefix.length(), prefix, expectedCenter, suffix, prefix, actualCenter, suffix);
    }

    private static int firstDifferentLine(final List<String> expectedList, final List<String> actualList) {
        for (int i = 0; i < expectedList.size(); i++) {
            if (!expectedList.get(i).equals(actualList.get(i))) {
                return i;
            }
        }
        throw new RuntimeException("no differences !");
    }

    private static String greatestCommonPrefix(String a, String b) {
        final int minLength = Math.min(a.length(), b.length());
        for (int i = 0; i < minLength; i++) {
            if (a.charAt(i) != b.charAt(i)) {
                return a.substring(0, i);
            }
        }
        return a.substring(0, minLength);
    }

    private static String greatestCommonSuffix(String a, String b) {
        final int aLength = a.length();
        final int bLength = b.length();
        final int minLength = Math.min(aLength, bLength);
        for (int i = 0; i < minLength; i++) {
            if (a.charAt(aLength - i - 1) != b.charAt(bLength - i - 1)) {
                return a.substring(aLength - i);
            }
        }
        return a.substring(aLength - minLength);
    }
}
