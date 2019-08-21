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


import org.junit.jupiter.api.Test;

import static com.github.writethemfirst.approvals.utils.StringUtils.describeDifferences;
import static org.assertj.core.api.Assertions.assertThat;

class StringUtilsTest {


    @Test
    void shouldDescribeDifferencesWhenSingleLine() {
        assertThat(describeDifferences("expected", "actual"))
            .isEqualTo("first difference at line#0 col#0: expected [expected] but was [actual]");
    }

    @Test
    void shouldDescribeDifferencesWhenDifferentLineCount() {
        assertThat(describeDifferences("expected1\nexpected2", "actual"))
            .isEqualTo("expected:\n" +
                "expected1\n" +
                "expected2\n" +
                "(2 lines) but was:\n" +
                "actual\n" +
                "(1 lines)");
    }

    @Test
    void shouldDescribeDifferencesWhenBuriedInText() {
        assertThat(describeDifferences("text1\ntext 2", "text1\ntext\t12"))
            .isEqualTo("expected:\n" +
                "text1\n" +
                "text 2\n" +
                " but was:\n" +
                "text1\n" +
                "text\t12\n" +
                "first difference at line#1 col#4: expected text[ ]2 but was text[\t1]2");
    }
}
