package com.github.writethemfirst.approvals.reporters.commands;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Optional;

import static java.lang.String.format;
import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.createFile;
import static java.nio.file.Paths.get;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Files.newTemporaryFolder;

class CommandTest {

    private final String OS_SEPARATOR = FileSystems.getDefault().getSeparator();

    @Test
    void shouldLocateIntelliJ() throws Exception {
        File temp = touchIdeaExe();
        Command command = new Command(
            temp + OS_SEPARATOR + "JetBrains",
            "idea64.exe");
        Optional<String> pathToExe = command.pathToExe();
        String expectedPath = format("JetBrains%sIntelliJ IDEA 2017.1.2%sbin%sidea64.exe", OS_SEPARATOR, OS_SEPARATOR, OS_SEPARATOR);
        assertThat(pathToExe).get().toString().endsWith(expectedPath);
    }

    private File touchIdeaExe() throws Exception {
        File temp = newTemporaryFolder();
        Path ideaFolder = createDirectories(get(temp.toString(), "JetBrains", "IntelliJ IDEA 2017.1.2", "bin"));
        Path ideaExe = ideaFolder.resolve("idea64.exe");
        createFile(ideaExe);
        return temp;
    }

    @Test
    void shouldNotLocateIntelliJ() {
        File temp = newTemporaryFolder();
        Optional<String> command = new Command(temp.toString(), "idea64.exe").pathToExe();
        assertThat(command).isEmpty();
    }
}
