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

package org.ds.simple.ink.launcher.settings.fragment;

import android.content.Context;

import org.ds.simple.ink.launcher.R;
import org.ds.simple.ink.launcher.sorting.SortingType;

import java.util.ArrayList;
import java.util.List;

import lombok.val;

public class AppDrawerSortingFragment extends SingleChoiceDialogFragment<SortingType> {

    AppDrawerSortingFragment(final String key) {
        super(key);
    }

    @Override
    protected List<SortingType> readCurrentItems(final Context context) {
        val resources = getResources();
        val labels = resources.getStringArray(R.array.sorting_type_labels);
        val values = resources.getStringArray(R.array.sorting_type_values);
        val icons = resources.obtainTypedArray(R.array.sorting_type_icons);

        val sortingTypes = new ArrayList<SortingType>();
        for (int i = 0; i < labels.length; ++i) {
            sortingTypes.add(new SortingType(labels[i], values[i], icons.getDrawable(i)));
        }
        icons.recycle();
        return sortingTypes;
    }
}