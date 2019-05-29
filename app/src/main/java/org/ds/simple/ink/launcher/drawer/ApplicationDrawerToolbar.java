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

package org.ds.simple.ink.launcher.drawer;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.ds.simple.ink.launcher.LauncherSettingsActivity;
import org.ds.simple.ink.launcher.R;

import lombok.val;

public class ApplicationDrawerToolbar extends RelativeLayout implements ApplicationDrawer.OnTotalItemCountChangeListener {

    private TextView totalItemsView;

    public ApplicationDrawerToolbar(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.app_drawer_toolbar, this);

        val launcherSettings = findViewById(R.id.open_launcher_settings);
        launcherSettings.setOnClickListener(new OnLauncherSettingsListener(getContext()));
    }

    public void setTotalItemsCount(final int itemsCount) {
        getTotalItemsView().setText(getResources().getString(R.string.total_item_count_label, itemsCount));
    }

    @Override
    public void totalCountChanged(int newCount) {
        setTotalItemsCount(newCount);
    }

    private TextView getTotalItemsView() {
        if (totalItemsView == null) {
            totalItemsView = findViewById(R.id.total_items_count);
        }
        return totalItemsView;
    }

    private static class OnLauncherSettingsListener implements View.OnClickListener {

        private final Context context;

        OnLauncherSettingsListener(final Context context) {
            this.context = context;
        }

        @Override
        public void onClick(final View launchSettingsButton) {
            val myIntent = new Intent(context, LauncherSettingsActivity.class);
            context.startActivity(myIntent);
        }
    }
}