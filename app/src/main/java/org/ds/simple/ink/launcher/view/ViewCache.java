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

package org.ds.simple.ink.launcher.view;

import android.util.SparseArray;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import lombok.NonNull;
import lombok.val;

public class ViewCache {

    private final View parent;
    private final SparseArray<View> cachedChildren = new SparseArray<>();

    public ViewCache(@NonNull final View parent) {
        this.parent = parent;
    }

    public View getView(final int id) {
        return getView(id, View.class);
    }

    public TextView getTextView(final int id) {
        return getView(id, TextView.class);
    }

    public ImageView getImageView(final int id) {
        return getView(id, ImageView.class);
    }

    public CheckBox getCheckBox(final int id) {
        return getView(id, CheckBox.class);
    }

    public RadioButton getRadioButton(final int id) {
        return getView(id, RadioButton.class);
    }

    private <T extends View> T getView(final int id, final Class<T> type) {
        val child = cachedChildren.get(id);
        if (child == null) {
            T newEntry = parent.findViewById(id);
            cachedChildren.put(id, newEntry);
            return newEntry;
        }
        return type.cast(child);
    }
}