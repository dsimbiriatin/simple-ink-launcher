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

import org.ds.simple.ink.launcher.BaseLauncherActivity;
import org.ds.simple.ink.launcher.apps.ApplicationInfo;
import org.ds.simple.ink.launcher.settings.preference.MultiChoiceListPreference;
import org.ds.simple.ink.launcher.settings.preference.PreferenceListItemAdapter;

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
        return PreferenceListItemAdapter.multiSelect(context, currentItems);
    }

    @Override
    public void onDialogClosed(final boolean positiveResult) {
        if (positiveResult) {
            val selected = from(listSelections.getSelections())
                    .transform(ApplicationInfo::getValue)
                    .toSet();

            getPreference().storeNewSelections(selected);
        }
    }
}