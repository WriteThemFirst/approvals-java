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
package com.github.writethemfirst.approvals.reporters.softwares;

import com.github.writethemfirst.approvals.Reporter;
import com.github.writethemfirst.approvals.reporters.FirstWorkingReporter;
import com.github.writethemfirst.approvals.reporters.JUnit5Reporter;
import com.github.writethemfirst.approvals.reporters.ThrowsReporter;

/**
 * Defines `Reporter`s which work on any OS.
 */
public interface Generic {

    String os = System.getProperty("os.name");
    boolean isWindows = os.startsWith("Windows");

    Reporter DEFAULT = isWindows
        ? Windows.DEFAULT
        : new FirstWorkingReporter(new JUnit5Reporter(), new ThrowsReporter());
}
