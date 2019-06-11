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

package org.ds.simple.ink.launcher.sorting.strategy;

import com.google.common.collect.ImmutableList;

import org.ds.simple.ink.launcher.apps.ApplicationInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Collections;
import java.util.List;

import static org.ds.simple.ink.launcher.utils.ComponentNameUtils.fromFlattenString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

@RunWith(RobolectricTestRunner.class)
public class ReaderApplicationFirstTest {

    @Test
    public void shouldOrderApplicationInfosByLabelHavingDefaultReaderApplicationFirst() {
        // given
        final List<ApplicationInfo> unorderedApplicationInfoList = ImmutableList.of(
                ApplicationInfo.builder().componentName(fromFlattenString("test.android/MessagesActivity")).build(),
                ApplicationInfo.builder().componentName(fromFlattenString("test.android/BrowserActivity")).build(),
                ApplicationInfo.builder().componentName(fromFlattenString("test.android/PhoneActivity")).build(),
                ApplicationInfo.builder().componentName(fromFlattenString("test.android/GalleryActivity")).build()
        );

        // when: setting Phone activity as default reader application
        Collections.sort(unorderedApplicationInfoList, new ReaderApplicationFirst("test.android/PhoneActivity"));

        // then: it will be first on the list
        assertThat(unorderedApplicationInfoList.get(0).getFlattenName(), containsString("PhoneActivity"));
    }
}