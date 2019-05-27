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

import com.github.writethemfirst.approvals.Reporter;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ConfiguredReporterTest {
    @Test
    void shouldDefaultReporterWhenAllCommented() {
        final ConfiguredReporter reporter = new ConfiguredReporter(
            "# /Applications/kdiff3.app/Contents/MacOS/kdiff3 //// %received% %approved% -o %approved%\n" +
                "# /usr/local/bin/idea //// diff %received% %approved%");

        assertThat(reporter.isAvailable()).isTrue();
        assertThat(reporter.delegate).isEqualTo(Reporter.BASIC);
    }

    @Test
    void shouldUseFirstReporterConfiguredInFile() {
        final ConfiguredReporter reporter = new ConfiguredReporter(
            "# /Applications/kdiff3.app/Contents/MacOS/kdiff3 //// %received% %approved% -o %approved%\n" +
                "/usr/local/bin/idea //// diff %received% %approved%");


        assertThat(reporter.isAvailable()).isTrue();
        assertThat(((CommandReporter) reporter.delegate).executableCommand.executable).isEqualTo("/usr/local/bin/idea");
    }
}
