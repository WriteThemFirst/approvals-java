package com.github.writethemfirst.approvals.reporters.commands;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static java.lang.Runtime.getRuntime;
import static java.lang.System.getenv;
import static java.nio.file.FileVisitOption.FOLLOW_LINKS;
import static java.nio.file.Files.find;
import static java.nio.file.Paths.get;
import static java.util.Arrays.stream;
import static java.util.Comparator.naturalOrder;
import static java.util.Optional.empty;
import static java.util.stream.Stream.concat;
import static java.util.stream.Stream.of;

/**
 * Wrapper around an executable command outside the JVM.
 */
public class Command {
    private static final int MAX_FOLDERS_DEPTH = 5;
    static String WINDOWS_ENV_PROGRAM_FILES = "ProgramFiles";
    public static final String PROGRAM_FILES_KEY = "%programFiles%";

    private final Runtime runtime;
    private final Map<String, String> env;

    private final String path;
    private final String executable;
    private Optional<String> cachedLatestPath;

    /**
     * Represents the latest version of the executable found by scanning subfolders of path. The path will
     * have %programFiles% replaced by the actual value in the environment variable `ProgramFiles`.
     */
    public Command(String path, String executable) {
        this(path, executable, getRuntime(), getenv());
    }

    /**
     * Only use this constructor from test code so the environment Map and Runtime can be mocked.
     */
    Command(String path, String executable, Runtime runtime, Map<String, String> env) {
        this.path = path;
        this.executable = executable;
        this.runtime = runtime;
        this.env = env;
    }

    /**
     * Runs the executable outside the JVM by calling Runtime.exec().
     */
    public void execute(String... arguments) throws IOException {
        String[] cmdArray = concat(
            of(pathToLatestExe().get()),
            stream(arguments))
            .toArray(String[]::new);
        runtime.exec(cmdArray);
    }

    /**
     * Tests if an executable file was found in the path.
     */
    public boolean available() {
        return pathToLatestExe().isPresent();
    }

    /**
     * Finds the latest version of an installed software.
     *
     * Sort order is based on folder names, assuming that latest version have a greater version number.
     */
    Optional<String> pathToLatestExe() {
        if (cachedLatestPath == null) {
            cachedLatestPath = searchForExe();
        }
        return cachedLatestPath;
    }

    private Optional<String> searchForExe() {
        try {
            return matchingCommands()
                .map(Path::toString)
                .max(naturalOrder());
        } catch (IOException e) {
            System.err.println(e);
            return empty();
        }
    }

    private Stream<Path> matchingCommands() throws IOException {
        return find(
            get(path.replace(PROGRAM_FILES_KEY, env.get(WINDOWS_ENV_PROGRAM_FILES))),
            MAX_FOLDERS_DEPTH,
            (p, a) -> p.endsWith(executable),
            FOLLOW_LINKS);
    }
}
