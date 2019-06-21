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

import org.ds.simple.ink.launcher.R;
import org.ds.simple.ink.launcher.common.ViewCache;

import lombok.NonNull;

public class MultiChoiceListItem extends PreferenceListItemView implements Checkable {

    private final ViewCache children = new ViewCache(this);

    public MultiChoiceListItem(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setIcon(@NonNull final Drawable icon) {
        children.getImageView(R.id.item_icon).setImageDrawable(icon);
    }

    @Override
    public void setLabel(@NonNull final String label) {
        children.getTextView(R.id.item_label).setText(label);
    }

    @Override
    public void setChecked(final boolean checked) {
        children.getCheckBox(R.id.item_selector).setChecked(checked);
    }

    @Override
    public boolean isChecked() {
        return children.getCheckBox(R.id.item_selector).isChecked();
    }

    @Override
    public void toggle() {
        children.getCheckBox(R.id.item_selector).toggle();
    }
}