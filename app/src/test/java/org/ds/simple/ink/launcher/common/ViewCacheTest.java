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

package org.ds.simple.ink.launcher.common;

import android.util.SparseArray;
import android.view.View;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import lombok.val;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(RobolectricTestRunner.class)
public class ViewCacheTest {

    @Test
    public void shouldCacheChildViewWhichWasRequestedPreviously() {
        // given
        val parent = givenParentView();
        val cachedChildren = new SparseArray<View>();
        val viewCache = new ViewCache(parent, cachedChildren);

        // when: getting View for the first time
        val firstTime = viewCache.getView(1);

        // then: requesting child from parent view
        then(parent).should().findViewById(1);
        assertThat(firstTime, is(notNullValue()));
        assertThat(cachedChildren.size(), is(1));

        // when: getting View for the second time
        val secondTime = viewCache.getView(1);

        // then: this view will be returned from the cache
        verifyNoMoreInteractions(parent);
        assertThat(secondTime, is(notNullValue()));
        assertThat(cachedChildren.size(), is(1));
    }

    private View givenParentView() {
        val parent = mock(View.class);
        given(parent.findViewById(1)).willReturn(mock(View.class));
        return parent;
    }
}