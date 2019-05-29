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

package org.ds.simple.ink.launcher.settings.preference;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.ds.simple.ink.launcher.R;

import lombok.NonNull;

public class SingleChoiceListItem extends RelativeLayout implements Checkable {

    private TextView label;
    private ImageView icon;
    private RadioButton itemSelector;

    public SingleChoiceListItem(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public void setIcon(@NonNull final Drawable icon) {
        getIcon().setImageDrawable(icon);
    }

    public void setLabel(@NonNull final String label) {
        getLabel().setText(label);
    }

    @Override
    public void setChecked(final boolean checked) {
        getItemSelector().setChecked(checked);
    }

    @Override
    public boolean isChecked() {
        return getItemSelector().isChecked();
    }

    @Override
    public void toggle() {
        getItemSelector().toggle();
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

    private RadioButton getItemSelector() {
        if (itemSelector == null) {
            itemSelector = findViewById(R.id.item_selector);
        }
        return itemSelector;
    }
}