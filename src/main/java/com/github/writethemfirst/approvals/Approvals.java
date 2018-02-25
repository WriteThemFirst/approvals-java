package com.github.writethemfirst.approvals;

import com.github.writethemfirst.approvals.reporters.softwares.Generic;
import com.github.writethemfirst.approvals.reporters.softwares.Windows;

import static com.github.writethemfirst.approvals.utils.StackUtils.callerClass;

/**
 * # Approvals
 *
 * *Approval Testing* is a way of considering Unit Tests without focusing on writing tons of assertions, but by letting
 * a tool actually dealing with that validation.
 *
 * `Approvals` is this framework's main entry point for computing validation of data produced by your program, by
 * comparing it to data you already reviewed and approved manually.
 *
 * Using `Approvals` for checking your program's data will actually trigger a comparison between the output it produces,
 * and content stored in a file somewhere in your project. If that file doesn't exist, the framework will warn you and
 * propose you to review the data produced by your program to approve (or not) what has been generated. After you
 * reviewed it and selected the proper output, it'll use the resulting file for future comparisons.
 *
 * Creating a `Approvals` object requires you to specify a `Class` to be linked with the object. It is used in order to
 * automatically compute the *approved* files to be used for storing the output data of your program. You can then
 * provide as many `Reporter` objects as you'd like. Those will be used if differences are found between your program's
 * output and the *approved* files' content. Some *Reporters* are already provided by the framework, but you surely can
 * provide some of your own.
 *
 * The basic and main entry point you should have a look at in this `Approvals` class is the `verify(_)` method,
 * allowing to compute a comparison between an output and some content stored in an *approved* file.
 *
 * @author mdaviot / aneveux
 * @version 1.0
 * @see Reporter
 */
public class Approvals {

    private final ApprovalsFiles approvalsFiles;
    private final Reporter reporter;

    /**
     * Constructs an `Approvals` object.
     *
     * A default `Reporter` will be created and used by the created `Approvals` instance.
     * @see Reporter
     * @see Generic
     */
    public Approvals() {
        this(Generic.DEFAULT);
    }

    /**
     * Constructs an `Approvals` object.
     *
     * @param reporter The reporter to trigger in case of differences between the output and the files' content.
     * @see Reporter
     */
    public Approvals(final Reporter reporter) {
        this(callerClass(Approvals.class), reporter);
    }


    /**
     * Constructs an `Approvals` object.
     *
     * @param clazz    The test class performing the assertions. It is used in order to compute the *approved* files'
     *                 names. It can be omitted and will be inferred to be the class calling the constructor.
     * @param reporter The reporter to trigger in case of differences between the output and the files' content.
     * @see Reporter
     */
    public Approvals(final Class<?> clazz, final Reporter reporter) {
        approvalsFiles = new ApprovalsFiles(clazz);
        this.reporter = reporter;
    }


    /**
     * Computes the comparison between the output of your program (taking place of this function's argument) and the
     * content of the *approved* file matching with the test method.
     *
     * It'll use a temporary file (containing the ouput of the program) which will be erased in case the results are
     * matching. Otherwise, that file will be kept for you to review it.
     *
     * In case of differences found in the output, all the `Reporter` objects linked to that `Approvals` instance will
     * be called.
     *
     * @param output Any object representing the output of your program. That will be compared to the associated
     *               *approved* file.
     * @throws Throwable Probably in case a `Reporter` is trying to do something it isn't capable of doing.
     */
    public void verify(final Object output)
        throws Throwable {
        if (matchesApprovedFile(output)) {
            approvalsFiles.removeReceived();
        } else {
            reporter.mismatch(approvalsFiles.approvedFile(), approvalsFiles.receivedFile());
        }
    }


    /**
     * Compares the *Program Under Tests*' output to the content of the *approved* file and checks for any differences.
     *
     * @param output Any object representing the output of *Program  Under Tests*. A `String` representation of that
     *               object will be computed using `toString()` and will be used for the comparison with the *approved*
     *               file's content.
     * @return true if the provided output perfectly matches with the existing *approved* file
     */
    private boolean matchesApprovedFile(final Object output) {
        final String approvedContent = approvalsFiles.readApproved();
        approvalsFiles.writeReceived(output.toString());
        return approvedContent != null && approvedContent.equals(output.toString());
    }

}
