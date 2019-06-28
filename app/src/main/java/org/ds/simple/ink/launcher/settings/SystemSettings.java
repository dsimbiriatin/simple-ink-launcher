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

package org.ds.simple.ink.launcher.settings;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import lombok.NonNull;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.content.Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED;
import static android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS;
import static android.provider.Settings.System.SCREEN_BRIGHTNESS;
import static android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE;
import static android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL;
import static org.ds.simple.ink.launcher.utils.Exceptions.execute;

@SuppressLint("InlinedApi")
public class SystemSettings {

    public static final int BRIGHTNESS_MIN = 0;
    public static final int BRIGHTNESS_MAX = 255;

    private final Context applicationContext;
    private final Intent manageWriteSettings;
    {
        this.manageWriteSettings = new Intent(ACTION_MANAGE_WRITE_SETTINGS);
        manageWriteSettings.setData(Uri.parse("package:org.ds.simple.ink.launcher"));
        manageWriteSettings.setFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
    }

    public SystemSettings(@NonNull final Context context) {
        this.applicationContext = context.getApplicationContext();
    }

    public void setScreenBrightnessTo(final int value) {
        Settings.System.putInt(getContentResolver(), SCREEN_BRIGHTNESS, value);
    }

    public int getScreenBrightnessValue() {
        return execute(() -> Settings.System.getInt(getContentResolver(), SCREEN_BRIGHTNESS)).or(-1);
    }

    public void enableManualBrightnessControl() {
        Settings.System.putInt(getContentResolver(), SCREEN_BRIGHTNESS_MODE, SCREEN_BRIGHTNESS_MODE_MANUAL);
    }

    public void requestWritePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            applicationContext.startActivity(manageWriteSettings);
        }
    }

    /**
     * On systems which support API version 23 and higher,
     * a separate permit is required to update system settings.
     */
    public boolean hasWritePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.System.canWrite(applicationContext);
        }
        return true;
    }

    private ContentResolver getContentResolver() {
        return applicationContext.getContentResolver();
    }
}