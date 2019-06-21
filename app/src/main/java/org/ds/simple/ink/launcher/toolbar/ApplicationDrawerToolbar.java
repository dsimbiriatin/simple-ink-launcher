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
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import org.ds.simple.ink.launcher.LauncherSettingsActivity;
import org.ds.simple.ink.launcher.R;
import org.ds.simple.ink.launcher.drawer.ApplicationDrawer;
import org.ds.simple.ink.launcher.settings.ApplicationSettings;
import org.ds.simple.ink.launcher.common.ViewCache;

import lombok.val;

public class ApplicationDrawerToolbar extends RelativeLayout implements ApplicationDrawer.OnTotalItemCountChangeListener,
                                                                        ApplicationSettings.OnWifiSwitchEnabledChangeListener,
                                                                        ApplicationSettings.OnBacklightSwitchEnabledChangeListener {
    private final ViewCache children = new ViewCache(this);

    public ApplicationDrawerToolbar(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.app_drawer_toolbar, this);
        initLauncherSettingsButton();
    }

    private void initLauncherSettingsButton() {
        val launcherSettings = children.getView(R.id.open_launcher_settings);
        launcherSettings.setOnClickListener(new OnLauncherSettingsListener());
    }

    public void showWifiSwitch(final boolean visible) {
        val visibility = getVisibilityForState(visible);
        children.getView(R.id.wifi_switch).setVisibility(visibility);
    }

    public void showBacklightSwitch(final boolean visible) {
        val visibility = getVisibilityForState(visible);
        children.getView(R.id.backlight_switch).setVisibility(visibility);
    }

    private int getVisibilityForState(final boolean visible) {
        return visible ? View.VISIBLE : View.GONE;
    }

    public void setTotalItemsCount(final int itemsCount) {
        val text = getResources().getString(R.string.total_item_count_label, itemsCount);
        children.getTextView(R.id.total_items_count).setText(text);
    }

    @Override
    public void totalCountChanged(final int newCount) {
        setTotalItemsCount(newCount);
    }

    @Override
    public void wifiSwitchEnabled(final boolean whetherEnabled) {
        showWifiSwitch(whetherEnabled);
    }

    @Override
    public void backlightSwitchEnabled(final boolean whetherEnabled) {
        showBacklightSwitch(whetherEnabled);
    }

    private class OnLauncherSettingsListener implements View.OnClickListener {

        @Override
        public void onClick(final View launchSettingsButton) {
            val myIntent = new Intent(getContext(), LauncherSettingsActivity.class);
            getContext().startActivity(myIntent);
        }
    }
}