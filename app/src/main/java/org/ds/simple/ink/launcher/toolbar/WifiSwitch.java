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

import android.content.Context;
import android.net.wifi.WifiManager;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageButton;

import org.ds.simple.ink.launcher.R;

import lombok.RequiredArgsConstructor;
import lombok.val;

import static android.content.Context.WIFI_SERVICE;

public class WifiSwitch extends AppCompatImageButton {

    public WifiSwitch(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        val wifiToasts = new WifiToasts(context);
        val wifiManager = getWifiManager(context);
        setImageBasedOn(wifiManager.isWifiEnabled());
        setOnClickListener(new WifiController(this, wifiToasts, wifiManager));
    }

    void setImageBasedOn(final boolean wifiState) {
        setImageResource(wifiState
                ? R.drawable.ic_wifi_on_toolbar
                : R.drawable.ic_wifi_off_toolbar);
    }

    private WifiManager getWifiManager(final Context context) {
        val appContext = context.getApplicationContext();
        return (WifiManager) appContext.getSystemService(WIFI_SERVICE);
    }

    @RequiredArgsConstructor
    static class WifiController implements View.OnClickListener {

        private final WifiSwitch wifiSwitch;
        private final WifiToasts wifiToasts;
        private final WifiManager wifiManager;

        @Override
        public void onClick(final View view) {
            val nextState = !wifiManager.isWifiEnabled();
            wifiSwitch.setImageBasedOn(nextState);
            wifiManager.setWifiEnabled(nextState);
            wifiToasts.newToast(nextState).show();
        }
    }
}