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
import org.ds.simple.ink.launcher.BaseLauncherActivity;
import org.ds.simple.ink.launcher.common.BaseListAdapter;
import org.ds.simple.ink.launcher.apps.ApplicationInfo;
import org.ds.simple.ink.launcher.settings.preference.MultiChoiceListItem;
import org.ds.simple.ink.launcher.settings.preference.MultiChoiceListPreference;

import java.util.List;

import lombok.val;

import static com.google.common.collect.FluentIterable.from;

public class HiddenApplicationsFragment extends BaseDialogFragment<ApplicationInfo, MultiChoiceListPreference> {

    HiddenApplicationsFragment(final String key) {
        super(key);
        setClickOnItemDismissEnabled(false);
    }

    @Override
    protected List<ApplicationInfo> readCurrentItems(final Context context) {
        return ((BaseLauncherActivity) context).getApplicationRepository().listAll(false);
    }

    @Override
    protected ListAdapter createListAdapter(final Context context, final List<ApplicationInfo> currentItems) {
        return new HiddenApplicationsAdapter(context, currentItems);
    }

    @Override
    protected void restorePreviousSelections(final ListView listView, final List<ApplicationInfo> currentItems) {
        val preference = getPreference();
        for (int i = 0; i < currentItems.size(); ++i) {
            val applicationInfo = currentItems.get(i);
            if (preference.wasSelectedPreviously(applicationInfo.getFlattenName())) {
                listView.setItemChecked(i, true);
            }
        }
    }

    @Override
    public void onDialogClosed(final boolean positiveResult) {
        if (positiveResult) {
            val selected = from(listSelections.getSelections())
                    .transform(ApplicationInfo::getFlattenName)
                    .toSet();

            getPreference().storeNewSelections(selected);
        }
    }

    private static class HiddenApplicationsAdapter extends BaseListAdapter<MultiChoiceListItem, ApplicationInfo> {

        private HiddenApplicationsAdapter(final Context context, final List<ApplicationInfo> items) {
            super(context, R.layout.multi_select_list_item, items);
        }

        @Override
        protected MultiChoiceListItem getView(final MultiChoiceListItem itemView, final ApplicationInfo item) {
            itemView.setIcon(item.getIcon());
            itemView.setLabel(item.getLabel());
            return itemView;
        }
    }
}