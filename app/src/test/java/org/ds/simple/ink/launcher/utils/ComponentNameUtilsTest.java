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
import org.robolectric.RobolectricTestRunner;

import lombok.val;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(RobolectricTestRunner.class)
public class ComponentNameUtilsTest {

    @Test
    public void shouldReturnEmptyPackageNameForInvalidFlattenComponentString() {
        // when: passing null
        val resultForNull = ComponentNameUtils.packageNameFrom(null);
        assertThat(resultForNull, is(""));

        // when: passing empty string
        val resultForEmpty = ComponentNameUtils.packageNameFrom("");
        assertThat(resultForEmpty, is(""));

        // when: passing string with invalid format
        val resultForInvalidFormat = ComponentNameUtils.packageNameFrom("com.abc.MyActivity");
        assertThat(resultForInvalidFormat, is(""));
    }

    @Test
    public void shouldExtractPackageNameFromComponentNameFlattenString() {
        // when: flatten component name string follows pattern 'com.abc/MyActivity'
        val packageName = ComponentNameUtils.packageNameFrom("com.abc/MyActivity");

        // then
        assertThat(packageName, is("com.abc"));
    }

    @Test
    public void shouldReturnDummyComponentNameForInvalidFlattenString() {
        // when: passing null
        val resultForNull = ComponentNameUtils.fromFlattenString(null);
        assertThat(resultForNull, is(ComponentNameUtils.DUMMY_COMPONENT));

        // when: passing empty string
        val resultForEmpty = ComponentNameUtils.fromFlattenString("");
        assertThat(resultForEmpty, is(ComponentNameUtils.DUMMY_COMPONENT));

        // when: passing string with invalid format
        val resultForInvalidFormat = ComponentNameUtils.fromFlattenString("com.abc.MyActivity");
        assertThat(resultForInvalidFormat, is(ComponentNameUtils.DUMMY_COMPONENT));
    }

    @Test
    public void shouldBuildComponentNameFromFlattenString() {
        // when: flatten component name string follows pattern 'com.abc/MyActivity'
        val componentName = ComponentNameUtils.fromFlattenString("com.abc/MyActivity");

        // then
        assertThat(componentName.getPackageName(), is("com.abc"));
        assertThat(componentName.getClassName(), is("MyActivity"));
    }
}