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

import lombok.val;

import static android.content.Context.WIFI_SERVICE;

public class WifiSwitch extends AppCompatImageButton {

    private final WifiToasts wifiToasts;
    private final WifiManager wifiManager;

    public WifiSwitch(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        this.wifiManager = getWifiManager(context);
        this.wifiToasts = new WifiToasts(context);
        setOnClickListener(new WifiController());
    }

    private WifiManager getWifiManager(final Context context) {
        val appContext = context.getApplicationContext();
        return (WifiManager) appContext.getSystemService(WIFI_SERVICE);
    }

    private class WifiController implements View.OnClickListener {

        @Override
        public void onClick(final View view) {
            val enabled = !wifiManager.isWifiEnabled();
            wifiManager.setWifiEnabled(enabled);
            wifiToasts.newToast(enabled).show();
        }
    }
}