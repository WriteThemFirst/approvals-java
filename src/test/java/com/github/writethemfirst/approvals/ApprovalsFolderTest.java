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
package com.github.writethemfirst.approvals;

import com.github.writethemfirst.approvals.files.Approbation;
import com.github.writethemfirst.approvals.files.ApprovalsFiles;
import com.github.writethemfirst.approvals.reporters.ThrowsReporter;
import com.github.writethemfirst.approvals.utils.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.github.writethemfirst.approvals.utils.FileUtils.write;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

class ApprovalsFolderTest {
    private Approvals approvals = new Approvals(new ThrowsReporter());
    private Approbation approbation = new Approbation();
    private Path sample = Paths.get("sample.xml");
    private Path sample2 = Paths.get("sample2.xml");
    Reporter reporter = mock(Reporter.class);


    @Test
    void shouldDoNothingWhenBothFoldersAreEmpty() throws IOException {
        //GIVEN
        Files.createDirectories(approbation.defaultFiles().approvalsFolder());
        Path parent = Files.createTempDirectory("shouldDoNothingWhenBothFoldersAreEmpty");

        //WHEN
        approvals.verifyAgainstMasterFolder(parent);

        //THEN no exception should be thrown

    }

    @Test
    void shouldThrowWhenAFileIsMissing() throws IOException {
        Path parent = Files.createTempDirectory("shouldThrowWhenAFileIsMissing");

        assertThatThrownBy(() -> approvals.verifyAgainstMasterFolder(parent))
            .isInstanceOf(AssertionError.class)
            .hasMessageContaining("expected: <some content> but was: <>");

    }

    @Test
    void shouldThrowWhenAFileIsDifferent() throws IOException {
        Path parent = Files.createTempDirectory("shouldThrowWhenAFileIsDifferent");
        Files.createFile(parent.resolve("sample.xml"));
        assertThatThrownBy(() -> approvals.verifyAgainstMasterFolder(parent))
            .isInstanceOf(AssertionError.class)
            .hasMessageContaining("expected: <expected content> but was: <>");

    }

    @Test
    void shouldFireReporterOnEachMismatch() throws IOException {
        Approvals approvals = new Approvals(reporter);

        Path parent = Files.createTempDirectory("shouldFireReporterOnEachMismatch");
        Files.createFile(parent.resolve(sample));
        Files.createFile(parent.resolve(sample2));
        ApprovalsFiles context = approbation.defaultFiles();

        try {
            approvals.verifyAgainstMasterFolder(parent);
        } catch (AssertionError e) {
            // expected
        }

        then(reporter).should().mismatch(
            Paths.get("src\\test\\resources\\com\\github\\writethemfirst\\approvals\\ApprovalsFolderTest\\shouldFireReporterOnEachMismatch.approved\\sample.xml"),
            Paths.get("src\\test\\resources\\com\\github\\writethemfirst\\approvals\\ApprovalsFolderTest\\shouldFireReporterOnEachMismatch.received\\sample.xml"));
        then(reporter).should().mismatch(
            Paths.get("src\\test\\resources\\com\\github\\writethemfirst\\approvals\\ApprovalsFolderTest\\shouldFireReporterOnEachMismatch.approved\\sample2.xml"),
            Paths.get("src\\test\\resources\\com\\github\\writethemfirst\\approvals\\ApprovalsFolderTest\\shouldFireReporterOnEachMismatch.received\\sample2.xml"));

        context.received.remove(sample);
        context.received.remove(sample2);
    }

    @Test
    void shouldCreateAllReceivedFiles() throws IOException {
        Approvals approvals = new Approvals(reporter);

        Path parent = Files.createTempDirectory("shouldCreateAllReceivedFiles");
        write("actual", parent.resolve("sample.xml"));
        write("actual2", parent.resolve("sample2.xml"));

        try {
            approvals.verifyAgainstMasterFolder(parent);
        } catch (AssertionError e) {
            // expected
        }

        Path approved1 = Paths.get("src\\test\\resources\\com\\github\\writethemfirst\\approvals\\ApprovalsFolderTest\\shouldCreateAllReceivedFiles.received\\sample.xml");
        assertThat(approved1)
            .hasContent("actual");
        Path approved2 = Paths.get("src\\test\\resources\\com\\github\\writethemfirst\\approvals\\ApprovalsFolderTest\\shouldCreateAllReceivedFiles.received\\sample2.xml");
        assertThat(approved2)
            .hasContent("actual2");

        FileUtils.silentRemove(approved1);
        FileUtils.silentRemove(approved2);
    }


    @Test
    void shouldRemoveMatchedReceivedFiles() throws IOException {
        Approvals approvals = new Approvals(reporter);

        Path parent = Files.createTempDirectory("shouldRemoveMatchedReceivedFiles");
        write("actual", parent.resolve("sample.xml"));
        ApprovalsFiles context = approbation.defaultFiles();
        write("actual", Paths.get("src\\test\\resources\\com\\github\\writethemfirst\\approvals\\ApprovalsFolderTest\\shouldRemoveMatchedReceivedFiles.approved\\sample.xml"));
        write("actual", Paths.get("src\\test\\resources\\com\\github\\writethemfirst\\approvals\\ApprovalsFolderTest\\shouldRemoveMatchedReceivedFiles.received\\sample.xml"));

        approvals.verifyAgainstMasterFolder(parent);

        assertThat(context.received.get(sample)).doesNotExist();

        context.received.remove(sample);
        context.approved.remove(sample);
    }

//    @Test
//    void shouldThrowOnReceivedFilesNotExpected() throws IOException {
//        Approvals approvals = new Approvals(new ThrowsReporter());
//
//        Path parent = Files.createTempDirectory("shouldThrowOnReceivedFilesNotExpected");
//        FileUtils.write("actual", parent.resolve("sample.xml"));
//        ApprovalsFiles customFiles = approbation.defaultFiles();
//
//        assertThatThrownBy(() -> approvals.verifyAgainstMasterFolder(parent))
//            .isInstanceOf(AssertionError.class)
//            .hasMessageContaining("");
//
//        customFiles.remove(sample);
//        customFiles.remove(sample);
//    }
}
