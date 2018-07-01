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

import com.github.writethemfirst.approvals.approvers.Approver;
import com.github.writethemfirst.approvals.testutils.FolderTestUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

class ApprovalsPathTest {
    private final Reporter mockReporter = mock(Reporter.class);
    private final Approver approver = new Approver().reportTo(mockReporter);

    @Test
    void shouldDoNothingWhenBothFoldersAreEmpty() throws IOException {
        //GIVEN
        final FolderTestUtils testUtils = new FolderTestUtils("shouldDoNothingWhenBothFoldersAreEmpty", getClass());

        //WHEN
        approver.verify(testUtils.actual);

        //THEN no exception should be thrown

        testUtils.cleanupPaths();
    }

    @Test
    void shouldThrowWhenAFileIsMissing() throws IOException {
        final FolderTestUtils testUtils = new FolderTestUtils("shouldThrowWhenAFileIsMissing", getClass());
        testUtils.writeApproved("some content", "someFile.txt");

        assertThatThrownBy(() -> approver.verify(testUtils.actual))
            .isInstanceOf(AssertionError.class)
            .hasMessageContaining("expected: <some content> but was: <>");

        testUtils.cleanupPaths();
    }

    @Test
    void shouldThrowWhenAnExtraFileIsPresent() throws IOException {
        final FolderTestUtils testUtils = new FolderTestUtils("shouldThrowWhenAnExtraFileIsPresent", getClass());
        testUtils.writeActual("some content", "someFile.txt");

        assertThatThrownBy(() -> approver.verify(testUtils.actual))
            .isInstanceOf(AssertionError.class)
            .hasMessageContaining("expected: <> but was: <some content>");

        testUtils.cleanupPaths();
    }

    @Test
    void shouldThrowWhenAFileIsDifferent() throws IOException {
        final FolderTestUtils testUtils = new FolderTestUtils("shouldThrowWhenAFileIsDifferent", getClass());
        testUtils.writeApproved("expected content", "sample.xml");

        assertThatThrownBy(() -> approver.verify(testUtils.actual))
            .isInstanceOf(AssertionError.class)
            .hasMessageContaining("expected: <expected content> but was: <>");

        testUtils.cleanupPaths();
    }

    @Test
    void shouldFireReporterOnceForAllMismatches() throws IOException {
        final Approver approvals = new Approver().reportTo(mockReporter);
        final FolderTestUtils testUtils = new FolderTestUtils("shouldFireReporterOnceForAllMismatches", getClass());

        testUtils.writeApproved("approved1", "sample.xml");
        testUtils.writeApproved("approved2", "sample2.xml");

        try {
            approvals.verify(testUtils.actual);
        } catch (final AssertionError e) {
            // expected
        }

        then(mockReporter).should().mismatch(testUtils.approvalFiles);

        testUtils.cleanupPaths();
    }

    @Test
    void shouldCreateAllReceivedFiles() throws IOException {
        final FolderTestUtils testUtils = new FolderTestUtils("shouldCreateAllReceivedFiles", getClass());

        final Approver approvals = new Approver().reportTo(mockReporter);

        testUtils.writeActual("actual", "sample.xml");
        testUtils.writeActual("actual2", "sample2.xml");

        try {
            approvals.verify(testUtils.actual);
        } catch (final AssertionError e) {
            // expected
        }


        assertThat(testUtils.received.resolve("sample.xml")).hasContent("actual");
        assertThat(testUtils.received.resolve("sample2.xml")).hasContent("actual2");

        testUtils.cleanupPaths();
    }


    @Test
    void shouldRemoveMatchedReceivedFiles() throws IOException {
        final Approver approvals = new Approver().reportTo(mockReporter);

        final FolderTestUtils testUtils = new FolderTestUtils("shouldRemoveMatchedReceivedFiles", getClass());
        testUtils.writeActual("actual", "sample.xml");
        testUtils.writeApproved("actual", "sample.xml");
        testUtils.writeReceived("actual", "sample.xml");

        approvals.verify(testUtils.actual);

        assertThat(testUtils.received.resolve("sample.xml")).doesNotExist();

        testUtils.cleanupPaths();
    }


    @Test
    void shouldThrowOnReceivedFilesNotExpected() throws IOException {
        final FolderTestUtils testUtils = new FolderTestUtils("shouldThrowOnReceivedFilesNotExpected", getClass());
        testUtils.cleanupPaths();
        testUtils.writeActual("actual", "sample.xml");

        assertThatThrownBy(() -> approver.verify(testUtils.actual))
            .isInstanceOf(AssertionError.class)
            .hasMessageContaining("expected: <> but was: <actual>");

        testUtils.cleanupPaths();
    }

    @Test
    void shouldCreateApprovedFiles() throws IOException {
        final FolderTestUtils testUtils = new FolderTestUtils("shouldCreateApprovedFiles", getClass());
        testUtils.writeActual("actual", "sample.xml");

        try {
            approver.verify(testUtils.actual);
        } catch (final AssertionError e) {
            // expected
        }

        assertThat(testUtils.approved.resolve("sample.xml")).exists().hasContent("actual");

        testUtils.cleanupPaths();
    }
}
