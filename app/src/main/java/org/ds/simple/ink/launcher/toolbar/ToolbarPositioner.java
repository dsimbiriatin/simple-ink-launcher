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

import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import org.ds.simple.ink.launcher.LauncherMainActivity;
import org.ds.simple.ink.launcher.R;
import org.ds.simple.ink.launcher.settings.ApplicationSettings;

import lombok.NonNull;
import lombok.val;

import static androidx.constraintlayout.widget.ConstraintSet.BOTTOM;
import static androidx.constraintlayout.widget.ConstraintSet.PARENT_ID;
import static androidx.constraintlayout.widget.ConstraintSet.TOP;

public class ToolbarPositioner implements ApplicationSettings.OnToolbarLocationChangeListener {

    private final ConstraintLayout activityLayout;
    private final ConstraintSet defaultConstraintSet;

    public ToolbarPositioner(@NonNull final LauncherMainActivity activity) {
        this.activityLayout = activity.getContentLayout();
        this.defaultConstraintSet = new ConstraintSet();
        defaultConstraintSet.clone(activityLayout);
    }

    public void positionTo(@NonNull final ToolbarLocationName locationName) {
        val constraintSet = new ConstraintSet();
        constraintSet.clone(defaultConstraintSet);

        if (locationName == ToolbarLocationName.HIDDEN) {
            constraintSet.connect(R.id.apps_grid, TOP, PARENT_ID, TOP);
            constraintSet.connect(R.id.apps_grid, BOTTOM, PARENT_ID, BOTTOM);
            constraintSet.setVisibility(R.id.app_drawer_toolbar, View.GONE);

        }  else if (locationName == ToolbarLocationName.TOP) {
            constraintSet.connect(R.id.app_drawer_toolbar, TOP, PARENT_ID, TOP);
            constraintSet.connect(R.id.apps_grid, TOP, R.id.app_drawer_toolbar, BOTTOM);
            constraintSet.connect(R.id.apps_grid, BOTTOM, PARENT_ID, BOTTOM);

        } else if (locationName == ToolbarLocationName.BOTTOM) {
            constraintSet.connect(R.id.apps_grid, TOP, PARENT_ID, TOP);
            constraintSet.connect(R.id.apps_grid, BOTTOM, R.id.app_drawer_toolbar, TOP);
            constraintSet.connect(R.id.app_drawer_toolbar, BOTTOM, PARENT_ID, BOTTOM);

        }
        constraintSet.applyTo(activityLayout);
    }

    @Override
    public void toolbarLocationChanged(final ToolbarLocationName newLocation) {
        positionTo(newLocation);
    }
}