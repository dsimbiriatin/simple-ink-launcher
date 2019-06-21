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
import android.content.res.TypedArray;
import android.util.AttributeSet;

import org.ds.simple.ink.launcher.R;

import lombok.NonNull;

public class SingleChoiceListPreference extends LauncherDialogPreference {

    private String previousSelection = "";

    public SingleChoiceListPreference(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        setPersistent(true);
    }

    @Override
    public int getDialogLayoutResource() {
        return R.layout.single_select_list;
    }

    @Override
    protected Object onGetDefaultValue(final TypedArray attributes, final int index) {
        return attributes.getString(index);
    }

    @Override
    public boolean wasSelectedPreviously(@NonNull final String value) {
        return previousSelection.equals(value);
    }

    @Override
    protected void onSetInitialValue(final Object defaultValue) {
        this.previousSelection = getPersistedString((String) defaultValue);
    }

    public void storeNewSelection(@NonNull final String newSelection) {
        this.previousSelection = newSelection;
        persistString(newSelection);
    }
}