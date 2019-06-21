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

import org.ds.simple.ink.launcher.apps.ApplicationInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Collections;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(JUnit4.class)
public class NameInAscOrderTest {

    @Test
    public void shouldOrderApplicationInfosByLabelInAscendingOrder() {
        // given
        final List<ApplicationInfo> unorderedApplicationInfoList = newArrayList(
                ApplicationInfo.builder().label("Messages").build(),
                ApplicationInfo.builder().label("Browser").build(),
                ApplicationInfo.builder().label("Phone").build(),
                ApplicationInfo.builder().label("Gallery").build()
        );

        // when
        Collections.sort(unorderedApplicationInfoList, new NameInAscOrder());

        // then
        assertThat(unorderedApplicationInfoList.get(0).getLabel(), is("Browser"));
        assertThat(unorderedApplicationInfoList.get(1).getLabel(), is("Gallery"));
        assertThat(unorderedApplicationInfoList.get(2).getLabel(), is("Messages"));
        assertThat(unorderedApplicationInfoList.get(3).getLabel(), is("Phone"));
    }
}