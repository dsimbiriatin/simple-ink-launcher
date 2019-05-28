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

package org.ds.simple.ink.launcher.settings.fragment;

import android.app.Dialog;
import android.content.res.Resources;

import lombok.val;

final class DialogFragmentSize {

    private DialogFragmentSize() {
        // do not instantiate
    }

    static void resize(final Dialog dialog, final float relativeWidth, final float relativeHeight) {
        if (dialog == null || dialog.getWindow() == null) {
            // nothing to do here
            return;
        }

        val window = dialog.getWindow();
        val currentWidth = window.getDecorView().getWidth();
        val currentHeight = window.getDecorView().getHeight();

        val potentialWidth = Math.round(getDisplayWidth() * relativeWidth);
        val potentialHeight = Math.round(getDisplayHeight() * relativeHeight);

        val actualWidth = (currentWidth < potentialWidth) ? currentWidth : potentialWidth;
        val actualHeight = (currentHeight < potentialHeight) ? currentHeight : potentialHeight;
        window.setLayout(actualWidth, actualHeight);
    }


    private static int getDisplayWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    private static int getDisplayHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
}