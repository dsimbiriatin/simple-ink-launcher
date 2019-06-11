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
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageButton;

import org.ds.simple.ink.launcher.R;
import org.ds.simple.ink.launcher.settings.SystemSettings;

import lombok.RequiredArgsConstructor;
import lombok.val;

import static org.ds.simple.ink.launcher.settings.SystemSettings.BRIGHTNESS_MAX;
import static org.ds.simple.ink.launcher.settings.SystemSettings.BRIGHTNESS_MIN;

public class BacklightSwitch extends AppCompatImageButton {

    private final SystemSettings settings;

    public BacklightSwitch(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        this.settings = new SystemSettings(context);
        setOnClickListener(new BacklightController(settings, this));
    }

    AlertDialog writePermissionRequestDialog() {
        val resources = getResources();
        return new AlertDialog.Builder(getContext())
                .setTitle(resources.getString(R.string.write_permissions_request_title))
                .setMessage(resources.getString(R.string.write_permissions_request_message))
                .setNeutralButton(resources.getString(R.string.ok_button), (d, w) -> settings.requestWritePermission())
                .create();
    }

    @RequiredArgsConstructor
    static class BacklightController implements View.OnClickListener {

        private int previous;
        private final SystemSettings settings;
        private final BacklightSwitch backlightSwitch;

        @Override
        public void onClick(final View view) {
            if (settings.hasWritePermission()) {
                settings.enableManualBrightnessControl();
                settings.setScreenBrightnessTo(nextValue());
            } else {
                backlightSwitch.writePermissionRequestDialog().show();
            }
        }

        /**
         * Possible states:
         * - Current brightness is not minimal -> switching to minimal
         * - Current brightness is minimal -> try to switch to previous value:
         *    - Previous value is minimal one -> switching to maximal brightness
         *    - Previous value is not minimal one -> switching to previously selected brightness
         */
        private int nextValue() {
            val current = settings.getScreenBrightnessValue();
            val actual = (current <= BRIGHTNESS_MIN) ? previousValue() : BRIGHTNESS_MIN;
            this.previous = current;
            return actual;
        }

        private int previousValue() {
            return (previous <= BRIGHTNESS_MIN) ? BRIGHTNESS_MAX : previous;
        }
    }
}