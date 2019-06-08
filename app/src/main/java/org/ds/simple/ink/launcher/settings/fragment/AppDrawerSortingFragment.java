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
import org.ds.simple.ink.launcher.common.BaseListAdapter;
import org.ds.simple.ink.launcher.settings.preference.SingleChoiceListItem;
import org.ds.simple.ink.launcher.settings.preference.SingleChoiceListPreference;
import org.ds.simple.ink.launcher.sorting.SortingType;

import java.util.ArrayList;
import java.util.List;

import lombok.val;

public class AppDrawerSortingFragment extends BaseDialogFragment<SortingType, SingleChoiceListPreference> {

    AppDrawerSortingFragment(final String key) {
        super(key);
        setClickOnItemDismissEnabled(true);
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

    @Override
    protected ListAdapter createListAdapter(final Context context, final List<SortingType> currentItems) {
        return new AppDrawerSortingTypeAdapter(context, currentItems);
    }

    @Override
    protected void restorePreviousSelections(final ListView listView, final List<SortingType> currentItems) {
        val preference = getPreference();
        for (int i = 0; i < currentItems.size(); ++i) {
            val sortingType = currentItems.get(i);
            if (preference.wasSelectedPreviously(sortingType.getValue())) {
                listView.setItemChecked(i, true);
                break;
            }
        }
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            val selected = listSelections.getSelection();
            getPreference().storeNewSelection(selected.getValue());
        }
    }

    private static class AppDrawerSortingTypeAdapter extends BaseListAdapter<SingleChoiceListItem, SortingType> {

        private AppDrawerSortingTypeAdapter(final Context context, final List<SortingType> items) {
            super(context, R.layout.single_select_list_item, items);
        }

        @Override
        protected SingleChoiceListItem getView(final SingleChoiceListItem itemView, final SortingType item) {
            itemView.setIcon(item.getIcon());
            itemView.setLabel(item.getLabel());
            return itemView;
        }
    }
}