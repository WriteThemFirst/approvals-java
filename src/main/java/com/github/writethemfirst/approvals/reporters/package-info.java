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
/**
 * # Reporters
 *
 * *Approval Testing* relies on the principle of comparing the program's output with some content stored in files.
 *
 * In case of any differences found during those comparisons, actions should be taken: it can fial the tests, or trigger
 * some other actions the developers choose. Like writing some logs, or reporting it some other way.
 *
 * Additionally, *Approvals-Java* aims at providing some integration with *diff & merge* tools to help developers
 * reviewing their programs' outputs, and easily generate those precious *approved* files.
 *
 * That whole mechanism is handled by Reporters, which are registered to the framework and are then called and triggered
 * in case differences are found while computing the files comparisons.
 *
 * That package aims at containing all the default Reporters (which are not OS dependent).
 */
package com.github.writethemfirst.approvals.reporters;
