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
package com.github.writethemfirst.approvals.files;

import com.github.writethemfirst.approvals.Reporter;
import com.github.writethemfirst.approvals.approvers.Approver;
import com.github.writethemfirst.approvals.reporters.ThrowsReporter;
import com.github.writethemfirst.approvals.testutils.FolderTestUtils;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

class ApprovalsFileTest {
    private final Reporter mockReporter = mock(Reporter.class);


    @Test
    void shouldMismatchOnReceivedFile() throws Exception {
        final Approver approver = new Approver().reportTo(mockReporter);
        final FolderTestUtils testUtils = new FolderTestUtils("shouldMismatchOnReceivedFile", getClass());
        testUtils.writeActual("actual", "file.txt");
        try {
            approver.verify(testUtils.actual.resolve("file.txt"));
        } catch (final AssertionError e) {
            // expected
        }

        then(mockReporter).should().mismatch(
            testUtils.approvalFiles.resolve(Paths.get("file.txt")));

        testUtils.cleanupPaths();
    }

    @Test
    void shouldMismatchOnReceivedFileContent() throws Exception {
        final Approver approver = new Approver().reportTo(new ThrowsReporter());
        final FolderTestUtils testUtils = new FolderTestUtils("shouldMismatchOnReceivedFileContent", getClass());
        testUtils.writeActual("actual", "file.txt");

        assertThatThrownBy(() -> approver.verify(testUtils.actual.resolve("file.txt")))
            .isInstanceOf(AssertionError.class)
            .hasMessageContaining("expected: <> but was: <actual>");

        assertThat(testUtils.readReceived("file.txt")).isEqualTo("actual");

        testUtils.cleanupPaths();
    }

    @Test
    void shouldEscapeFileName() {
        final Path path = ApprovalFiles.approvalFilePath(Paths.get("folder"), "m-13#a:b_c+d*e/f g", "ext");
        assertThat(path).hasFileName("m-13_a_b_c_d_e_f_g.ext");
    }
}
