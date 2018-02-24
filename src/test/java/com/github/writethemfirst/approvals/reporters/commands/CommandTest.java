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
    private String IDEA_8 = "IntelliJ IDEA 2018";
    private String IDEA_712 = "IntelliJ IDEA 2017.1.2";
    private String IDEA_73 = "IntelliJ IDEA 2017.3";

    @Test
    void shouldLocateIntelliJ() throws Exception {
        File temp = newTemporaryFolder();
        touchIdeaExe(IDEA_712, temp);
        Command command = new Command(
            temp + OS_SEPARATOR + "JetBrains",
            "idea64.exe");
        Optional<String> pathToExe = command.pathToExe();
        String expectedPath = "JetBrains" + OS_SEPARATOR + IDEA_712 + OS_SEPARATOR + "bin" + OS_SEPARATOR + "idea64.exe";
        assertThat(pathToExe).get().toString().endsWith(expectedPath);
    }

    @Test
    void shouldLocateLatestIntelliJ() throws Exception {
        File temp = newTemporaryFolder();
        touchIdeaExe(IDEA_712, temp);
        touchIdeaExe(IDEA_8, temp);
        touchIdeaExe(IDEA_73, temp);
        Command command = new Command(
            temp + OS_SEPARATOR + "JetBrains",
            "idea64.exe");
        Optional<String> pathToExe = command.pathToExe();
        String expectedPath = "JetBrains" + OS_SEPARATOR + IDEA_8 + OS_SEPARATOR + "bin" + OS_SEPARATOR + "idea64.exe";
        assertThat(pathToExe.get()).endsWith(expectedPath);
    }

    private void touchIdeaExe(String version, File temp) throws Exception {
        Path ideaFolder = createDirectories(get(temp.toString(), "JetBrains", version, "bin"));
        Path ideaExe = ideaFolder.resolve("idea64.exe");
        createFile(ideaExe);
    }

    @Test
    void shouldNotLocateIntelliJ() {
        File temp = newTemporaryFolder();
        Optional<String> command = new Command(temp.toString(), "idea64.exe").pathToExe();
        assertThat(command).isEmpty();
    }
}
