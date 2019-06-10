/*
 * Simple Ink Launcher
 * Copyright (C) 2019  Dmitriy Simbiriatin <dmitriy.simbiriatin@gmail.com>
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.ds.simple.ink.launcher.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import lombok.val;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(JUnit4.class)
public class IntentUtilsTest {

    @Test
    public void shouldResultEmptyPackageNameForInvalidDataString() {
        // when: passing null
        val resultForNull = IntentUtils.packageNameFrom(null);
        assertThat(resultForNull, is(""));

        // when: passing empty string
        val resultForEmpty = IntentUtils.packageNameFrom("");
        assertThat(resultForEmpty, is(""));

        // when: passing string with invalid format
        val resultForInvalidFormat = IntentUtils.packageNameFrom("com.abc");
        assertThat(resultForInvalidFormat, is(""));
    }

    @Test
    public void shouldReturnPackageNameIfDataStringFollowsSpecificPattern() {
        // when: data string follows pattern 'package:<package name>'
        val packageName = IntentUtils.packageNameFrom("package:com.abc");

        // then
        assertThat(packageName, is("com.abc"));
    }
}