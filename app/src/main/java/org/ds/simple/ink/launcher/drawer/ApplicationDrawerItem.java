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
import android.widget.LinearLayout;

import org.ds.simple.ink.launcher.R;
import org.ds.simple.ink.launcher.apps.ApplicationInfo;
import org.ds.simple.ink.launcher.common.ViewCache;

import lombok.NonNull;
import lombok.val;

public class ApplicationDrawerItem extends LinearLayout {

    private final ViewCache children = new ViewCache(this);

    public ApplicationDrawerItem(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    @SuppressWarnings("SuspiciousNameCombination")
    public void onMeasure(final int width, final int height) {
        // Used 'width' for both height and width so the layout keeps square shape
        super.onMeasure(width, width);
    }

    public void updateWith(@NonNull final ApplicationInfo applicationInfo, final int iconSize) {
        val label = children.getTextView(R.id.item_label);
        label.setText(applicationInfo.getLabel());

        val icon = children.getImageView(R.id.item_icon);
        icon.setImageDrawable(applicationInfo.getIcon());

        val layoutParams = icon.getLayoutParams();
        layoutParams.width = iconSize;
        layoutParams.height = iconSize;
        icon.setLayoutParams(layoutParams);
    }
}