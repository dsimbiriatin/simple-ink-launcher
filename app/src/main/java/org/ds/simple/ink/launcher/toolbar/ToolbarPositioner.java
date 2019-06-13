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

import androidx.constraintlayout.widget.ConstraintSet;

import org.ds.simple.ink.launcher.LauncherMainActivity;

import lombok.NonNull;
import lombok.val;

import static androidx.constraintlayout.widget.ConstraintSet.BOTTOM;
import static androidx.constraintlayout.widget.ConstraintSet.PARENT_ID;
import static androidx.constraintlayout.widget.ConstraintSet.TOP;

public class ToolbarPositioner {

    private final LauncherMainActivity activity;

    public ToolbarPositioner(@NonNull final LauncherMainActivity activity) {
        this.activity = activity;
    }

    public void positionTo(@NonNull final ToolbarLocationName locationName) {
        val launcherLayout = activity.getContentLayout();
        val constraintSet = new ConstraintSet();
        constraintSet.clone(launcherLayout);

        position(locationName, constraintSet);
        constraintSet.applyTo(launcherLayout);
    }

    private void position(final ToolbarLocationName location, final ConstraintSet constraintSet) {
        constraintSet.connect(location.topViewId, TOP, PARENT_ID, TOP);
        constraintSet.connect(location.topViewId, BOTTOM, location.bottomViewId, TOP);
        constraintSet.connect(location.bottomViewId, BOTTOM, PARENT_ID, BOTTOM);
    }
}