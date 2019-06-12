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

import android.widget.ListView;

import org.ds.simple.ink.launcher.settings.preference.PreferenceListItem;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.val;

@AllArgsConstructor
class ListSelections<T extends PreferenceListItem> {

    private final ListView listView;

    boolean hasOneSelection() {
        return listView.getCheckedItemCount() == 1;
    }

    T getSelection() {
        val position = listView.getCheckedItemPosition();
        return getAt(position);
    }

    List<T> getSelections() {
        val selections = new ArrayList<T>();
        val positions = listView.getCheckedItemPositions();

        for (int i = 0; i <= listView.getCount(); ++i) {
            if (positions.get(i)) {
                selections.add(getAt(i));
            }
        }
        return selections;
    }

    @SuppressWarnings("unchecked")
    private T getAt(final int position) {
        return (T) listView.getItemAtPosition(position);
    }

    static <T extends PreferenceListItem> ListSelections<T> countSelectionsFor(final ListView applicationListView) {
        return new ListSelections<>(applicationListView);
    }
}