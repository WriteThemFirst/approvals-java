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
package com.github.writethemfirst.approvals.reporters.windows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static java.lang.Runtime.getRuntime;
import static java.lang.System.getenv;
import static java.nio.file.FileVisitOption.FOLLOW_LINKS;
import static java.nio.file.Paths.get;
import static java.util.Arrays.stream;
import static java.util.Comparator.naturalOrder;
import static java.util.stream.Stream.concat;
import static java.util.stream.Stream.of;

/**
 * Wrapper around an executable command outside the JVM.
 */
public class Command {
    private static final int MAX_FOLDERS_DEPTH = 5;
    static String WINDOWS_ENV_PROGRAM_FILES = "ProgramFiles";
    static String WINDOWS_ENV_PROGRAM_FILES_X86 = "ProgramFiles(x86)";
    public static final String PROGRAM_FILES_KEY = "%programFiles%";

    private final Runtime runtime;
    private final Map<String, String> env;

    private final String path;
    private final String executable;
    private Optional<String> cachedLatestPath;
    private final String programFilesFolder;
    private final String programFilesX86Folder;

    /**
     * Represents the latest version of the executable found by scanning subfolders of path. The path will have
     * %programFiles% replaced by the actual value in the environment variable `ProgramFiles`.
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
        programFilesFolder = env.get(WINDOWS_ENV_PROGRAM_FILES);
        programFilesX86Folder = env.get(WINDOWS_ENV_PROGRAM_FILES_X86);

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
    public boolean isAvailable() {
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
        Stream<Path> programFilesFolders = concat(replaced(programFilesFolder), replaced(programFilesX86Folder));
        Stream<Path> possiblePaths = concat(programFilesFolders, notReplaced());
        return possiblePaths
            .flatMap(this::matchingCommandInPath)
            .map(Path::toString)
            .max(naturalOrder());
    }

    private Stream<Path> matchingCommandInPath(Path possiblePath) {
        try {
            return Files.find(
                possiblePath,
                MAX_FOLDERS_DEPTH,
                (p, a) -> p.endsWith(executable),
                FOLLOW_LINKS);
        } catch (IOException e) {
            System.err.println(e);
            return Stream.empty();
        }
    }

    private Stream<Path> replaced(String folder) {
        if (folder == null) {
            return Stream.empty();
        } else {
            Path path = get(this.path.replace(PROGRAM_FILES_KEY, folder));
            return path.toFile().isDirectory()
                ? of(path)
                : Stream.empty();
        }
    }

    private Stream<Path> notReplaced() {
        if (path.contains(PROGRAM_FILES_KEY)) {
            return Stream.empty();
        } else {
            Path pat = get(path);
            return pat.toFile().isDirectory()
                ? of(pat)
                : Stream.empty();
        }
    }
}
