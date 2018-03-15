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

import com.github.writethemfirst.approvals.reporters.windows.Command;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

import static com.github.writethemfirst.approvals.reporters.windows.Command.*;
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
        final File temp = newTemporaryFolder();
        touchIdeaExe(IDEA_8, temp);
        final Runtime runtime = mock(Runtime.class);


        final Command command = new Command(
            temp + OS_SEPARATOR + "JetBrains",
            "idea64.exe",
            runtime,
            getenv());

        command.execute("merge", "toto.approved", "toto.received", "toto.approved");

        final String executable = temp + OS_SEPARATOR + "JetBrains" + OS_SEPARATOR + IDEA_8 + OS_SEPARATOR + "bin" + OS_SEPARATOR + "idea64.exe";
        then(runtime).should().exec(new String[]{executable, "merge", "toto.approved", "toto.received", "toto.approved"});
    }

    @Test
    void shouldLocateLatestIntelliJ() throws Exception {
        final File temp = newTemporaryFolder();
        touchIdeaExe(IDEA_712, temp);
        touchIdeaExe(IDEA_8, temp);
        touchIdeaExe(IDEA_73, temp);
        final Command command = new Command(
            temp + OS_SEPARATOR + "JetBrains",
            "idea64.exe");

        final Optional<String> pathToExe = command.pathToLatestExe();
        final boolean available = command.isAvailable();

        final String expectedPath = "JetBrains" + OS_SEPARATOR + IDEA_8 + OS_SEPARATOR + "bin" + OS_SEPARATOR + "idea64.exe";
        assertThat(pathToExe.get()).endsWith(expectedPath);
        assertThat(available).isTrue();

    }

    @Test
    void shouldLocateIntelliJInProgramFiles() throws Exception {
        //Temp folder and environment variables
        //Can be mocked more neatly with https://github.com/glytching/junit-extensions
        final File temp = newTemporaryFolder();
        touchIdeaExe(IDEA_8, temp);
        final Map<String, String> mockedEnv = mock(Map.class);
        when(mockedEnv.get(WINDOWS_ENV_PROGRAM_FILES)).thenReturn(temp.getAbsolutePath());

        final Command command = new Command(PROGRAM_FILES_KEY, "idea64.exe", mock(Runtime.class), mockedEnv);

        final boolean available = command.isAvailable();

        assertThat(available).isTrue();
    }

    @Test
    void shouldLocateExeInProgramFilesX86() throws Exception {
        //Temp folder and environment variables
        //Can be mocked more neatly with https://github.com/glytching/junit-extensions
        final File temp = newTemporaryFolder();
        touchIdeaExe(IDEA_8, temp);
        final Map<String, String> mockedEnv = mock(Map.class);
        when(mockedEnv.get(WINDOWS_ENV_PROGRAM_FILES_X86)).thenReturn(temp.getAbsolutePath());

        final Command command = new Command(PROGRAM_FILES_KEY, "idea64.exe", mock(Runtime.class), mockedEnv);

        final boolean available = command.isAvailable();

        assertThat(available).isTrue();
    }

    private void touchIdeaExe(final String version, final File temp) throws Exception {
        final Path ideaFolder = createDirectories(get(temp.toString(), "JetBrains", version, "bin"));
        final Path ideaExe = ideaFolder.resolve("idea64.exe");
        createFile(ideaExe);
    }

    @Test
    void shouldNotLocateIntelliJ() {
        final File temp = newTemporaryFolder();
        final Command command = new Command(temp.toString(), "idea64.exe");

        final Optional<String> latestExe = command.pathToLatestExe();
        final boolean available = command.isAvailable();

        assertThat(latestExe).isEmpty();
        assertThat(available).isFalse();
    }
}
