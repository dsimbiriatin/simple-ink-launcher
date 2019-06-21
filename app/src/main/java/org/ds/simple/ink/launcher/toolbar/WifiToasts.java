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

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import org.ds.simple.ink.launcher.R;

import lombok.NonNull;
import lombok.val;

import static android.widget.Toast.LENGTH_SHORT;

class WifiToasts {

    private final Context context;
    private String messageEnabled;
    private String messageDisabled;

    WifiToasts(@NonNull final Context context) {
        this.context = context;
        initToastMessages();
    }

    private void initToastMessages() {
        val resources = context.getResources();
        this.messageEnabled = resources.getString(R.string.wifi_enabled);
        this.messageDisabled = resources.getString(R.string.wifi_disabled);
    }

    @SuppressLint("ShowToast")
    Toast newToast(final boolean enabled) {
        val message = enabled ? messageEnabled : messageDisabled;
        val toast = Toast.makeText(context, message, LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        return toast;
    }
}