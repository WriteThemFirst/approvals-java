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

import com.github.writethemfirst.approvals.Approver;
import com.github.writethemfirst.approvals.testutils.SimpleTestUtils;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JUnit5ReporterTest {
    private final Approver approvals = new Approver().reportTo(new JUnit5Reporter());


    @Test
    void shouldThrowWhenMismatch() {
        final SimpleTestUtils testUtils = new SimpleTestUtils("shouldThrowWhenMismatch", getClass(), "");
        testUtils.writeApproved("some text");

        assertThatThrownBy(() -> approvals.verify("other text"))
            .isInstanceOf(AssertionError.class)
            .hasMessageContaining("expected: <some text> but was: <other text>");

        testUtils.cleanupPaths();
    }


    @Test
    void shouldThrowWhenMissing() {
        final SimpleTestUtils testUtils = new SimpleTestUtils("shouldThrowWhenMismatch", getClass(), "");
        testUtils.cleanupPaths();

        assertThatThrownBy(() -> approvals.verify("my text"))
            .isInstanceOf(AssertionError.class)
            .hasMessageContaining("expected: <> but was: <my text>");

        testUtils.cleanupPaths();
    }
}
