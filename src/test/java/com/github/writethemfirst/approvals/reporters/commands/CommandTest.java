package com.github.writethemfirst.approvals.reporters.commands;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

import static com.github.writethemfirst.approvals.reporters.commands.Command.*;
import static java.lang.System.getenv;
import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.createFile;
import static java.nio.file.Paths.get;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Files.newTemporaryFolder;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CommandTest {
    private final String OS_SEPARATOR = FileSystems.getDefault().getSeparator();
    private String IDEA_8 = "IntelliJ IDEA 2018";
    private String IDEA_712 = "IntelliJ IDEA 2017.1.2";
    private String IDEA_73 = "IntelliJ IDEA 2017.3";

    @Test
    void shouldExecuteIntelliJ() throws Exception {
        File temp = newTemporaryFolder();
        touchIdeaExe(IDEA_8, temp);
        Runtime runtime = mock(Runtime.class);


        Command command = new Command(
            temp + OS_SEPARATOR + "JetBrains",
            "idea64.exe",
            runtime,
            getenv());

        command.execute("merge", "toto.approved", "toto.received", "toto.approved");

        String executable = temp + OS_SEPARATOR + "JetBrains" + OS_SEPARATOR + IDEA_8 + OS_SEPARATOR + "bin" + OS_SEPARATOR + "idea64.exe";
        then(runtime).should().exec(new String[]{executable, "merge", "toto.approved", "toto.received", "toto.approved"});
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

        Optional<String> pathToExe = command.pathToLatestExe();
        boolean available = command.isAvailable();

        String expectedPath = "JetBrains" + OS_SEPARATOR + IDEA_8 + OS_SEPARATOR + "bin" + OS_SEPARATOR + "idea64.exe";
        assertThat(pathToExe.get()).endsWith(expectedPath);
        assertThat(available).isTrue();

    }

    @Test
    void shouldLocateIntelliJInProgramFiles() throws Exception {
        //Temp folder and environment variables
        //Can be mocked more neatly with https://github.com/glytching/junit-extensions
        File temp = newTemporaryFolder();
        touchIdeaExe(IDEA_8, temp);
        Map<String, String> mockedEnv = mock(Map.class);
        when(mockedEnv.get(WINDOWS_ENV_PROGRAM_FILES)).thenReturn(temp.getAbsolutePath());

        Command command = new Command(PROGRAM_FILES_KEY, "idea64.exe", mock(Runtime.class), mockedEnv);

        boolean available = command.isAvailable();

        assertThat(available).isTrue();
    }

    @Test
    void shouldLocateExeInProgramFilesX86() throws Exception {
        //Temp folder and environment variables
        //Can be mocked more neatly with https://github.com/glytching/junit-extensions
        File temp = newTemporaryFolder();
        touchIdeaExe(IDEA_8, temp);
        Map<String, String> mockedEnv = mock(Map.class);
        when(mockedEnv.get(WINDOWS_ENV_PROGRAM_FILES_X86)).thenReturn(temp.getAbsolutePath());

        Command command = new Command(PROGRAM_FILES_KEY, "idea64.exe", mock(Runtime.class), mockedEnv);

        boolean available = command.isAvailable();

        assertThat(available).isTrue();
    }

    private void touchIdeaExe(String version, File temp) throws Exception {
        Path ideaFolder = createDirectories(get(temp.toString(), "JetBrains", version, "bin"));
        Path ideaExe = ideaFolder.resolve("idea64.exe");
        createFile(ideaExe);
    }

    @Test
    void shouldNotLocateIntelliJ() {
        File temp = newTemporaryFolder();
        Command command = new Command(temp.toString(), "idea64.exe");

        Optional<String> latestExe = command.pathToLatestExe();
        boolean available = command.isAvailable();

        assertThat(latestExe).isEmpty();
        assertThat(available).isFalse();
    }
}
