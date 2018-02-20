package org.approvalsj.reporter.command;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Optional;

import static java.nio.file.FileVisitOption.FOLLOW_LINKS;
import static java.nio.file.Files.find;
import static java.nio.file.Paths.get;
import static java.util.Optional.empty;

public class Command {

    private final String path;
    private final String executable;

    public Command(String path, String executable) {
        this.path = path;
        this.executable = executable;
    }

    private  boolean found(Path p, BasicFileAttributes a) {
        return p.endsWith(executable);
    }

    public Optional<String> command() {
        try {
            return find(get(path), 5, this::found, FOLLOW_LINKS)
                    .map(Path::toString)
                    .findFirst();
        } catch (IOException e) {
            System.err.println(e);
            return empty();
        }
    }
}
