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

package org.ds.simple.ink.launcher.utils;

import android.content.Intent;

import androidx.annotation.NonNull;

import lombok.val;

import static com.google.common.base.Strings.nullToEmpty;

public final class IntentUtils {

    private IntentUtils() {
        // do not instantiate
    }

    /**
     * Extracts package name from {@link Intent#getDataString()}.
     */
    @NonNull
    public static String packageNameFrom(final String intentDataString) {
        val dataArray = nullToEmpty(intentDataString).split(":");
        return (dataArray.length == 2) ? dataArray[1] : "";
    }
}