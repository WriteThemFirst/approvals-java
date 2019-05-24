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

package com.github.writethemfirst.approvals.reporters;

import com.github.writethemfirst.approvals.Reporter;

import java.util.stream.Stream;

import static com.github.writethemfirst.approvals.reporters.windows.Windows.*;

public class ListAvailableReporters {
    public static void main(String[] args) {
        Stream.of(KDIFF,
            IDEA,
            TORTOISE_SVN,
            BEYOND_COMPARE_4,
            BEYOND_COMPARE_3,
            WINMERGE,
            ARAXIS,
            CODE_COMPARE,
            GVIM)
            .filter(Reporter::isAvailable)
            .forEach(System.out::println);
    }
}
