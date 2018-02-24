package com.github.writethemfirst.approvals.reporters;

/**
 * A reporter which executes an external command on Windows, aware of the "Program Files" folder.
 */
public class WindowsReporter extends ExecReporter {
    private final String programFiles = System.getenv("ProgramFiles");

    public WindowsReporter(String command) {
        super(command);
    }

    public WindowsReporter(String... command) {
        super(command);
    }

    /**
     * Replaces %programFiles% with the actual environment value.
     */
    @Override
    protected String osReplace(String elt) {
        return elt
            .replace("%programFiles%", programFiles == null ? "" : programFiles);
    }
}
