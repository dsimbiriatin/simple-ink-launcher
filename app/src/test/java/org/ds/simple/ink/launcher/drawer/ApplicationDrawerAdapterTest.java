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

package org.ds.simple.ink.launcher.drawer;

import android.content.Context;
import android.view.LayoutInflater;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import org.ds.simple.ink.launcher.apps.ApplicationInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;
import java.util.Set;

import lombok.val;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static org.ds.simple.ink.launcher.utils.ComponentNameUtils.fromFlattenString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
public class ApplicationDrawerAdapterTest {

    @Test
    public void shouldReturnItemsCountTakingInConsiderationAmountOfHiddenOnes() {
        // given
        val adapter = givenApplicationDrawerAdapter();

        // when: there are no hidden items selected
        val infos = givenApplicationInfos();
        adapter.setItems(infos);

        // then: all the provided items are available
        assertThat(adapter.getCount(), is(3));

        // when: first item is hidden
        val oneHiddenItem = givenOneHiddenItem();
        adapter.hideItems(oneHiddenItem);

        // then: only two items out of 3 are available
        assertThat(adapter.getCount(), is(2));
    }

    @Test
    public void shouldReturnItemAtCertainPositionTakingInConsiderationHiddenOnes() {
        // given
        val adapter = givenApplicationDrawerAdapter();

        // when: there are no hidden items selected
        val infos = givenApplicationInfos();
        adapter.setItems(infos);

        // then
        assertThat(adapter.getItem(1).getFlattenName(), is("com.cba/MyActivity2"));

        // when: first item is hidden
        val oneHiddenItem = givenOneHiddenItem();
        adapter.hideItems(oneHiddenItem);

        // then: positions are shifted
        assertThat(adapter.getItem(1).getFlattenName(), is("com.bca/MyActivity3"));
    }

    private ApplicationDrawerAdapter givenApplicationDrawerAdapter() {
        val context = mock(Context.class);
        given(context.getSystemService(LAYOUT_INFLATER_SERVICE)).willReturn(mock(LayoutInflater.class));
        return new ApplicationDrawerAdapter(context);
    }

    private Set<String> givenOneHiddenItem() {
        return ImmutableSet.of("com.abc/MyActivity1");
    }

    private List<ApplicationInfo> givenApplicationInfos() {
        return ImmutableList.of(
            ApplicationInfo.builder().componentName(fromFlattenString("com.abc/MyActivity1")).build(),
            ApplicationInfo.builder().componentName(fromFlattenString("com.cba/MyActivity2")).build(),
            ApplicationInfo.builder().componentName(fromFlattenString("com.bca/MyActivity3")).build()
        );
    }
}