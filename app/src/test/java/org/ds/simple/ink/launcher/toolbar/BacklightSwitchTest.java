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

package org.ds.simple.ink.launcher.toolbar;

import android.view.View;

import androidx.appcompat.app.AlertDialog;

import org.ds.simple.ink.launcher.settings.SystemSettings;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.stubbing.Answer;
import org.junit.runners.JUnit4;

import lombok.val;

import static org.ds.simple.ink.launcher.settings.SystemSettings.BRIGHTNESS_MAX;
import static org.ds.simple.ink.launcher.settings.SystemSettings.BRIGHTNESS_MIN;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willAnswer;
import static org.mockito.Mockito.mock;

@RunWith(JUnit4.class)
public class BacklightSwitchTest {

    @Test
    public void shouldSwitchToMinimalBrightnessWhenCurrentValueIsMoreThatMinimalOne() {
        // given
        val settings = givenSettingsWithBrightnessValue(50);
        val controller = new BacklightSwitch.BacklightController(settings, null);

        // when
        controller.onClick(mock(View.class));

        // then
        then(settings).should().enableManualBrightnessControl();
        then(settings).should().setScreenBrightnessTo(BRIGHTNESS_MIN);
    }

    @Test
    public void shouldSwitchBrightnessToMaxValueIfCurrentIsMinimalAndPreviousStateIsNotPresent() {
        // given: brightness controller which does not have previous state
        val settings = givenSettingsWithBrightnessValue(BRIGHTNESS_MIN);
        val controller = new BacklightSwitch.BacklightController(settings, null);

        // when
        controller.onClick(mock(View.class));

        // then
        then(settings).should().enableManualBrightnessControl();
        then(settings).should().setScreenBrightnessTo(BRIGHTNESS_MAX);
    }

    @Test
    public void shouldSwitchBrightnessFromMinimalValueToPreviousStateWhenPresent() {
        // given: brightness controller which does not have previous state yet
        val settings = givenSettingsWithBrightnessValue(30);
        val controller = new BacklightSwitch.BacklightController(settings, null);

        // when: switching state, brightness value = 30 will be stored as previous state
        controller.onClick(mock(View.class));

        // then
        then(settings).should().setScreenBrightnessTo(BRIGHTNESS_MIN);

        // when: switching state once again, brightness will be set to previous state
        controller.onClick(mock(View.class));

        // then
        then(settings).should().setScreenBrightnessTo(30);
    }

    @Test
    public void showsNotificationDialogWhenLauncherDoesNotHaveWritePermissionsToSystemSettings() {
        // given
        val backlightSwitch = givenDefaultBacklightSwitch();
        val settings = givenSystemSettingsWithoutPermissions();
        val controller = new BacklightSwitch.BacklightController(settings, backlightSwitch);

        // when
        controller.onClick(mock(View.class));

        // then
        then(backlightSwitch.writePermissionRequestDialog()).should().show();
    }

    private SystemSettings givenSettingsWithBrightnessValue(final int value) {
        val settings = systemSettingsWithPermissions();
        given(settings.getScreenBrightnessValue()).willReturn(value);
        return settings;
    }

    private SystemSettings givenSystemSettingsWithoutPermissions() {
        val settings = mock(SystemSettings.class);
        given(settings.hasWritePermission()).willReturn(false);
        return settings;
    }

    private BacklightSwitch givenDefaultBacklightSwitch() {
        val backlightSwitch = mock(BacklightSwitch.class);
        given(backlightSwitch.writePermissionRequestDialog()).willReturn(mock(AlertDialog.class));
        return backlightSwitch;
    }

    private SystemSettings systemSettingsWithPermissions() {
        val settings = mock(SystemSettings.class);
        given(settings.hasWritePermission()).willReturn(true);
        willAnswer(storingBrightnessValue(settings)).given(settings).setScreenBrightnessTo(anyInt());
        return settings;
    }

    private Answer<BDDMockito.BDDMyOngoingStubbing<Integer>> storingBrightnessValue(final SystemSettings settings) {
        return i -> given(settings.getScreenBrightnessValue()).willReturn(i.getArgument(0));
    }
}