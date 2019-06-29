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

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;

import org.ds.simple.ink.launcher.BaseLauncherActivity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import lombok.val;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;

@RunWith(JUnit4.class)
public class ActivityTypeAwareTest {

    private final TestActivityTypeAware activityTypeAwareObject = new TestActivityTypeAware();

    @Test
    public void shouldExtractActivityFromContextAtFirstLevel() {
        // given
        val context = mock(BaseLauncherActivity.class);

        // when
        val activity = activityTypeAwareObject.getActivity(context, BaseLauncherActivity.class);

        // then
        assertThat(activity, is(notNullValue()));
        then(context).should(never()).getBaseContext();
    }

    @Test
    public void shouldExtractActivityFromContextOnNestedLevel() {
        // given
        val context = mock(ContextWrapper.class);
        given(context.getBaseContext()).willReturn(mock(BaseLauncherActivity.class));

        // when
        val activity = activityTypeAwareObject.getActivity(context, BaseLauncherActivity.class);

        // then
        assertThat(activity, is(notNullValue()));
        then(context).should().getBaseContext();
    }

    @Test
    public void shouldReturnNothingForContextWhichIsNotContextWrapperImplementation() {
        // when
        val activity = activityTypeAwareObject.getActivity(mock(Context.class), Activity.class);

        // then
        assertThat(activity, is(nullValue()));
    }

    private static class TestActivityTypeAware implements ActivityTypeAware {}
}