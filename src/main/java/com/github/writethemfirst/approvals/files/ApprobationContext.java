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
package com.github.writethemfirst.approvals.files;

import java.nio.file.Path;

import static com.github.writethemfirst.approvals.utils.StackUtils.callerClass;
import static com.github.writethemfirst.approvals.utils.StackUtils.callerMethod;
import static java.lang.String.format;
import static java.nio.file.Paths.get;

/**
 * # ApprobationContext
 *
 * *Approval Testing* relies on comparing data produced by a *Program Under Tests* and data which has been reviewed and
 * validated by the developer. This validated data is stored in *approved* files along with the project, and is used for
 * comparisons with the data produced by the program's execution.
 *
 * Some {@link ApprovalsFiles} are required for performing *Approval Testing* though.
 *
 * Usually, the files are directly generated by the automated tests, and located along with the source code, most of the
 * time in the `src/test/resources` folder. Folders and files' names are most of the time directly computed by the
 * library which retrieves them from the execution customFiles by finding the test class name, and the method which
 * executed the tests. Developers don't have to worry about where the files will be located then.
 *
 * In order to keep that library compliant with other languages and other test frameworks though (like scalatest, or
 * kotlintest), we can't only rely on the library to detect from where the test has been executed, because in those case
 * it would simply fail and put all the files in an *unknown* folder. For those partiular cases, we introduced the
 * {@link ApprobationContext} object which allows to hold the callers of a test (both class and method), and will allow
 * to specify the method name in case of another language for example. This will allow to manage in a smart and
 * effective way the actual ApprovalsFiles.
 *
 * That {@link ApprobationContext} actually provides a way to retrieve defaultFiles, which will find the proper location
 * of class and methods on its own, and customFiles, which will let the developer specify the exact location on which
 * the files should be located.
 *
 * @author mdaviot / aneveux
 * @version 1.1
 */
public class ApprobationContext {

    private final Class<?> testClass;

    /**
     * Constructs an `ApprobationContext` by retrieving automatically the caller class using the {@link
     * com.github.writethemfirst.approvals.utils.StackUtils#callerClass(Class)}.
     */
    public ApprobationContext() {
        this(callerClass(ApprobationContext.class));
    }

    /**
     * Constructs an `ApprobationContext` by specifying manually the caller class.
     *
     * @param testClass The test class linked to this instance. Created files will contain that class name in their
     *                  path.
     */
    public ApprobationContext(final Class<?> testClass) {
        this.testClass = testClass;
    }

    /**
     * Returns the default *approved* and *received* files to be used in the current {@link ApprobationContext}. Default
     * files mean that their names will be computed from the automatically retrieved caller's method name.
     *
     * Warning: Usage of this method from Scala or Kotlin is discouraged. Please refer to customFiles method instead.
     *
     * @return Default {@link ApprovalsFiles} to be used in the current context by automatically retrieving the caller
     * method name
     */
    public ApprovalsFiles defaultFiles() {
        return new ApprovalsFiles(folderForClass(), callerMethodName());
    }

    /**
     * Returns the custom *approved* and *received* files to be used in the current {@link ApprobationContext}. Custom
     * files mean that their names will be deduced from the provided method name instead of automatically retrieving the
     * caller's method name.
     *
     * Warning: Usage of this method from Java is discouraged. Please refer to defaultFiles method instead.
     *
     * @param methodName The name of the caller test method which will be used to name the ApprovalsFiles
     * @return Custom {@link ApprovalsFiles} to be used in the current context. The files' names will be deduced from
     * the provided method name and not be retrieved automatically.
     */
    public ApprovalsFiles customFiles(final String methodName) {
        return new ApprovalsFiles(folderForClass(), methodName);
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

    /**
     * Returns the caller method name using {@link com.github.writethemfirst.approvals.utils.StackUtils}.
     *
     * It returns `unknown_method` in case the caller method cannot be retrieved automatically.
     *
     * Info: It doesn't return an option or null or an empty string, so the generated files are located in a visible
     * *unknown* file, which encourages the developer to solve the actual issue.
     *
     * @return the caller method name found by {@link com.github.writethemfirst.approvals.utils.StackUtils} or
     * `unknown_method` otherwise.
     */
    private String callerMethodName() {
        return callerMethod(testClass).orElse("unknown_method");
    }

}
