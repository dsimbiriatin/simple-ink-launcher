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

import android.net.wifi.WifiManager;
import android.view.View;
import android.widget.Toast;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import lombok.val;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.mock;

@RunWith(JUnit4.class)
public class WifiSwitchTest {

    @Test
    public void shouldSwitchWiFiStateOnOnClick() {
        // given: enabled wifi
        val wifiSwitch = givenWifiSwitch();
        val wifiToasts = givenWifiToasts();
        val wifiManager = givenWifiManagerWithWifiEnabled();
        val wifiController = new WifiSwitch.WifiController(wifiSwitch, wifiToasts, wifiManager);

        // when
        wifiController.onClick(mock(View.class));

        // then: wifi should be disabled
        then(wifiManager).should().setWifiEnabled(false);
        then(wifiToasts.newToast(false)).should().show();
        then(wifiSwitch).should().setImageBasedOn(false);
    }

    private WifiSwitch givenWifiSwitch() {
        val wifiSwitch = mock(WifiSwitch.class);
        willDoNothing().given(wifiSwitch).setImageBasedOn(anyBoolean());
        return wifiSwitch;
    }

    private WifiToasts givenWifiToasts() {
        val wifiToasts = mock(WifiToasts.class);
        given(wifiToasts.newToast(anyBoolean())).willReturn(mock(Toast.class));
        return wifiToasts;
    }

    private WifiManager givenWifiManagerWithWifiEnabled() {
        val wifiManager = mock(WifiManager.class);
        given(wifiManager.isWifiEnabled()).willReturn(true);
        return wifiManager;
    }
}