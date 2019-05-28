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

package org.ds.simple.ink.launcher;

import android.annotation.SuppressLint;

import androidx.appcompat.app.AppCompatActivity;

import org.ds.simple.ink.launcher.apps.ApplicationRepository;
import org.ds.simple.ink.launcher.apps.IconsThemeRepository;
import org.ds.simple.ink.launcher.settings.ApplicationSettings;

@SuppressLint("Registered")
public class BaseLauncherActivity extends AppCompatActivity {

    public final IconsThemeRepository getIconsThemeRepository() {
        return getLauncherApplication().getIconsThemeRepository();
    }

    public final ApplicationSettings getApplicationSettings() {
        return getLauncherApplication().getApplicationSettings();
    }

    public final ApplicationRepository getApplicationRepository() {
        return getLauncherApplication().getApplicationRepository();
    }

    public LauncherApplication getLauncherApplication() {
        return (LauncherApplication) getApplication();
    }
}