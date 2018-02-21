package com.github.writethemfirst.approvals.reporters.commands;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

import static java.nio.file.FileVisitOption.FOLLOW_LINKS;
import static java.nio.file.Files.find;
import static java.nio.file.Paths.get;
import static java.util.Optional.empty;

public class Command {
    private static final int MAX_FOLDERS_DEPTH = 5;
    private final String path;
    private final String executable;

    Command(String path, String executable) {
        this.path = path;
        this.executable = executable;
    }

    public Optional<String> pathToExe() {
        try {
            return matchingCommands()
                .map(Path::toString)
                .findFirst();
        } catch (IOException e) {
            System.err.println(e);
            return empty();
        }
    }

    private Stream<Path> matchingCommands() throws IOException {
        return find(
            get(path),
            MAX_FOLDERS_DEPTH,
            (p, a) -> p.endsWith(executable),
            FOLLOW_LINKS);
    }
}
