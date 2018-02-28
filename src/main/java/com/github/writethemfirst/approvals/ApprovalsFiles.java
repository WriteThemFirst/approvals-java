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
package com.github.writethemfirst.approvals;

import java.nio.file.Path;

import static com.github.writethemfirst.approvals.utils.StackUtils.callerClass;
import static com.github.writethemfirst.approvals.utils.StackUtils.callerMethod;
import static java.lang.String.format;
import static java.nio.file.Paths.get;

/**
 * # ApprovalsFiles
 *
 * *Approval Testing* relies on comparing data produced by a *Program Under Tests* and data which has been reviewed and
 * validated by the developer. This validated data is stored in *approved* files along with the project, and is used for
 * comparisons with the data produced by the program's execution.
 *
 * Two files are needed for computing *Approval Tests*: an *approved* file, which should be committed with the source
 * code, containing data validated by the developer, and a *received* file, which temporarily holds the data produced by
 * the *Program Under Tests* execution. Those files are then compared to validate the proper behavior of the program.
 *
 * That `ApprovalsFiles` class can be seen as a companion to a test class since it provides useful methods allowing to
 * read and write both *approved* and *received* files. It is linked to a test class since the produced files will be
 * named after the test class and its methods.
 *
 * An `ApprovalsFiles` object will be automatically created and attached to any created `Approvals` object, but you may
 * choose to instantiate one yourself for accessing and managing the *approved* and *received* files of a particular
 * class.
 *
 * @author mdaviot / aneveux
 * @version 1.0
 */
public class ApprovalsFiles {

    private final Class<?> testClass;

    /**
     * Constructs an `ApprovalsFiles` using the {@link com.github.writethemfirst.approvals.utils.StackUtils#callerClass(Class)}.
     */
    public ApprovalsFiles() {
        this(callerClass(ApprovalsFiles.class));
    }

    /**
     * Constructs an `ApprovalsFiles`.
     *
     * @param testClass The test class linked to this instance. Created files will contain that class name in their
     *                  path.
     */
    ApprovalsFiles(final Class<?> testClass) {
        this.testClass = testClass;
    }

    public ApprobationContext defaultContext() {
        return new ApprobationContext(folderForClass(), callerMethodName());
    }

    public ApprobationContext context(String methodName) {
        return new ApprobationContext(folderForClass(), methodName);
    }

    /**
     * Computes and returns the Path to the folder to be used for storing the *approved* and *received* files linked to
     * the `testClass` instance.
     *
     * The folder will be created under `src/test/resources` in the really same project, and will be named after the
     * package name of the `testClass`, followed by the name of the `testClass` itself. That folder will later contain
     * one pair of files (*approved* and *received*) for each method to be tested.
     *
     * @return The Path to the folder linked to the `testClass` attribute, used for storing the *received* and
     * *approved* files.
     */
    private Path folderForClass() {
        final String packageName = testClass.getPackage().getName();
        final Path packageResourcesPath = get("src/test/resources/", packageName.split("\\."));
        return packageResourcesPath.resolve(testClass.getSimpleName());
    }

    private String callerMethodName() {
        return callerMethod(testClass).orElse("unknown_method");
    }


}
