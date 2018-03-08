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

import com.github.writethemfirst.approvals.files.ApprobationContext;
import com.github.writethemfirst.approvals.files.ApprovalsFiles;
import org.junit.jupiter.api.Test;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;


class ReporterTest {
    private Reporter reporter = mock(Reporter.class);
    private Approvals approvals = new Approvals(reporter);
    private ApprobationContext approbationContext = new ApprobationContext();


    @Test
    void approvalsShouldCallReporterWhenMismatch() {
        ApprovalsFiles context = approbationContext.defaultFiles();
        context.approved.write("some text");

        try {
            approvals.verify("different text");
        } catch (AssertionError e) {
            // expected
        }

        then(reporter).should().mismatch(context.approved.get(), context.received.get());

        context.approved.remove();
        context.received.remove();
    }


    @Test
    void approvalsShouldCallReporterWhenNoApprovedFile() {
        ApprovalsFiles context = approbationContext.defaultFiles();

        context.approved.remove();

        try {
            approvals.verify("text");
        } catch (AssertionError e) {
            // expected
        }

        then(reporter).should().mismatch(context.approved.get(), context.received.get());

        context.approved.remove();
        context.received.remove();
    }

}
