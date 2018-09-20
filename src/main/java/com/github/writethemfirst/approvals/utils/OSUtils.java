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

/**
 * # OSUtils
 *
 * Approvals-Java can be executed from all kind of environments, which also means all kind of OS (at least Windows,
 * Linux, Mac OSX).
 *
 * That means some operations will have to be handled in different ways depending on which Operating System it is
 * executed on.
 *
 * That class aims at providing helper methods allowing to deal with OS specific operations.
 */
public class OSUtils {

    /**
     * The Operating System full name retrieved from the system properties.
     */
    static final String OPERATING_SYSTEM = System.getProperty("os.name");

    /**
     * Indicates if the current operating system is included in the Windows Family (all versions included).
     */
    public static boolean isWindows = OPERATING_SYSTEM.startsWith("Windows");

    /**
     * Indicates if the current operating system is included in the Mac OS Family (all versions included).
     */
    public static boolean isMacOs = OPERATING_SYSTEM.toLowerCase().startsWith("mac");

    /**
     * Indicates if the current operating system is included in the Linux Family (all distributions / versions included).
     */
    public static boolean isLinux = OPERATING_SYSTEM.startsWith("Linux");

}
