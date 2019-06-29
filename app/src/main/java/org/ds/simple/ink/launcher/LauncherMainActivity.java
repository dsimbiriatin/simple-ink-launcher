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

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;

import org.ds.simple.ink.launcher.drawer.ApplicationDrawer;
import org.ds.simple.ink.launcher.toolbar.ApplicationDrawerToolbar;
import org.ds.simple.ink.launcher.toolbar.ToolbarPositioner;

import lombok.val;

public class LauncherMainActivity extends BaseLauncherActivity {

    @SuppressWarnings("FieldCanBeLocal")
    private ToolbarPositioner toolbarPositioner;

    @SuppressWarnings("FieldCanBeLocal")
    private ApplicationDrawer applicationDrawer;

    @SuppressWarnings("FieldCanBeLocal")
    private ApplicationDrawerToolbar applicationDrawerToolbar;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        val applicationSettings = getApplicationSettings();
        val applicationRepository = getApplicationRepository();

        applicationDrawer = findViewById(R.id.apps_grid);
        applicationDrawer.setApplications(applicationRepository.listAll(false));
        applicationDrawer.sortApplications(applicationSettings.getSortingStrategy());
        applicationDrawer.hideApplications(applicationSettings.getHiddenApplications());
        applicationDrawer.applyMainScreenPreferences(applicationSettings.getMainScreenPreferences());

        applicationRepository.registerCacheUpdateListener(applicationDrawer);
        applicationSettings.registerSortingStrategyChangeListener(applicationDrawer);
        applicationSettings.registerHideApplicationsChangeListener(applicationDrawer);
        applicationSettings.registerMainScreenPreferencesChangeListener(applicationDrawer);

        applicationDrawerToolbar = findViewById(R.id.app_drawer_toolbar);
        applicationDrawerToolbar.setTotalItemsCount(applicationDrawer.getCount());
        applicationDrawerToolbar.showWifiSwitch(applicationSettings.showWifiSwitch());
        applicationDrawerToolbar.showBacklightSwitch(applicationSettings.showBacklightSwitch());

        applicationDrawer.registerOnTotalCountChangeListener(applicationDrawerToolbar);
        applicationSettings.registerWifiSwitchEnabledChangeListener(applicationDrawerToolbar);
        applicationSettings.registerBacklightSwitchEnabledChangeListener(applicationDrawerToolbar);

        toolbarPositioner = new ToolbarPositioner(this);
        toolbarPositioner.positionTo(applicationSettings.getToolbarLocation());
        applicationSettings.registerToolbarLocationChangeListener(toolbarPositioner);
    }

    @Override
    public void onBackPressed() {
        // block possibility to go back on launcher main activity
    }

    public ConstraintLayout getContentLayout() {
        return findViewById(R.id.launcher_layout_id);
    }
}