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
package com.github.writethemfirst.approvals.utils;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static com.github.writethemfirst.approvals.utils.StackUtils.callerClass;
import static com.github.writethemfirst.approvals.utils.StackUtils.callerMethod;
import static com.github.writethemfirst.approvals.utils.StackUtils.sanitizeClassName;
import static org.assertj.core.api.Assertions.assertThat;

class StackUtilsTest {
    @Test
    void guessedClassShouldBeTestClass() {
        final String guessed = StackUtils.callerClass(StackUtils.class);
        assertThat(guessed).isEqualTo(getClass().getName());
    }

    @Test
    void methodNameShouldBeEmpty() {
        assertThat(callerMethod(String.class.getName())).isEmpty();
    }


    @Test
    void methodNameShouldBeTheMethodName() {
        assertThat(callerMethod(getClass().getName())).contains("methodNameShouldBeTheMethodName");
    }

  @Test
    void callerClassShouldExcludeSeveralArguments() {
      assertThat(Utils.call()).isEqualTo(getClass().getName());
    }

    static class Utils {
        static String call(){
            return callerClass(StackUtils.class, Utils.class);
        }
    }

    @Test
    void sanitizeClassNameInLambda() {
        String className = getClass().getName();
        Stream.of("whatever")
            .forEach(s -> assertThat(sanitizeClassName(getClass().getName())).isEqualTo(className));
    }

    @Test
    void methodNameShouldNotBeLambda() {
        Stream.of("whatever")
            .forEach(s -> assertThat(callerMethod(getClass().getName())).contains("methodNameShouldNotBeLambda"));
    }
}
