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
import android.widget.ListAdapter;
import android.widget.ListView;

import org.ds.simple.ink.launcher.R;
import org.ds.simple.ink.launcher.settings.preference.PreferenceListItem;
import org.ds.simple.ink.launcher.settings.preference.PreferenceListItemAdapter;
import org.ds.simple.ink.launcher.settings.preference.SingleChoiceListPreference;

import java.util.List;

import lombok.val;

public abstract class SingleChoiceDialogFragment<T extends PreferenceListItem> extends BaseDialogFragment<T, SingleChoiceListPreference> {

    SingleChoiceDialogFragment(final String key) {
        super(key);
        setClickOnItemDismissEnabled(true);
    }

    @Override
    protected ListAdapter createListAdapter(final Context context, final List<T> currentItems) {
        return new PreferenceListItemAdapter<>(context, R.layout.single_select_list_item, currentItems);
    }

    @Override
    protected void restorePreviousSelections(final ListView listView, final List<T> currentItems) {
        val preference = getPreference();
        for (int i = 0; i < currentItems.size(); ++i) {
            val item = currentItems.get(i);
            if (preference.wasSelectedPreviously(item.getValue())) {
                listView.setItemChecked(i, true);
                break;
            }
        }
    }

    @Override
    public void onDialogClosed(final boolean positiveResult) {
        if (positiveResult && listSelections.hasOneSelection()) {
            val selected = listSelections.getSelection();
            getPreference().storeNewSelection(selected.getValue());
        }
    }
}