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
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.ds.simple.ink.launcher.R;
import org.ds.simple.ink.launcher.apps.ApplicationInfo;

public class ApplicationDrawerItem extends LinearLayout {

    private TextView label;
    private ImageView icon;

    public ApplicationDrawerItem(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    @SuppressWarnings("SuspiciousNameCombination")
    public void onMeasure(final int width, final int height) {
        // Used 'width' for both height and width so the layout keeps square shape
        super.onMeasure(width, width);
    }

    public void updateWith(final ApplicationInfo applicationInfo) {
        getIcon().setImageDrawable(applicationInfo.getIcon());
        getLabel().setText(applicationInfo.getLabel());
    }

    private ImageView getIcon() {
        if (icon == null) {
            icon = findViewById(R.id.item_icon);
        }
        return icon;
    }

    private TextView getLabel() {
        if (label == null) {
            label = findViewById(R.id.item_label);
        }
        return label;
    }
}