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
package com.github.writethemfirst.approvals.reporters;

import com.github.writethemfirst.approvals.Approvals;
import com.github.writethemfirst.approvals.ApprovalsFiles;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JUnit5ReporterTest {
    private Approvals approvals = new Approvals(new JUnit5Reporter());
    private ApprovalsFiles approvalsFiles = new ApprovalsFiles();


    @Test
    void shouldThrowWhenMismatch() {
        approvalsFiles.writeApproved("some text");

        assertThatThrownBy(() -> approvals.verify("other text"))
            .isInstanceOf(AssertionError.class)
            .hasMessageContaining("expected: <some text> but was: <other text>");

        approvalsFiles.removeApproved();
        approvalsFiles.removeReceived();
    }


    @Test
    void shouldThrowWhenMissing() {
        approvalsFiles.removeApproved();

        assertThatThrownBy(() -> approvals.verify("text"))
            .isInstanceOf(AssertionError.class)
            .hasMessageContaining("expected: <> but was: <text>");

        approvalsFiles.removeReceived();

    }
}
