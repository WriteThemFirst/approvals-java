package com.github.writethemfirst.approvals.reporters.commands;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Optional;

import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.createFile;
import static java.nio.file.Paths.get;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Files.newTemporaryFolder;

class CommandTest {

    private final String OS_SEPARATOR = FileSystems.getDefault().getSeparator();

    @Test
    void shouldLocateIntelliJ()
            throws Exception {

        File temp = touchIdeaExe();
        Optional<String> command = new Command(temp + OS_SEPARATOR + "JetBrains", "idea64.exe").command();
        assertThat(command).hasValueSatisfying(v -> v.endsWith("JetBrains" + OS_SEPARATOR + "IntelliJ IDEA 2017.1.2" + OS_SEPARATOR + "bin" + OS_SEPARATOR + "idea64.exe"));
    }


    private File touchIdeaExe()
            throws IOException {
        File temp = newTemporaryFolder();
        Path ideaFolder = createDirectories(get(temp.toString(), "JetBrains", "IntelliJ IDEA 2017.1.2", "bin"));
        Path ideaExe = ideaFolder.resolve("idea64.exe");
        createFile(ideaExe);
        return temp;
    }


    @Test
    void shouldNotLocateIntelliJ()
            throws Exception {
        File temp = newTemporaryFolder();
        Optional<String> command = new Command(temp.toString(), "idea64.exe").command();
        assertThat(command).isEmpty();
    }

}