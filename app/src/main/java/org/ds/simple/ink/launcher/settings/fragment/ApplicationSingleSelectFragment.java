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

import org.ds.simple.ink.launcher.apps.ApplicationInfo;
import org.ds.simple.ink.launcher.settings.preference.PreferenceListItemAdapter;
import org.ds.simple.ink.launcher.settings.preference.SingleChoiceListPreference;

import java.util.List;

import lombok.val;

public abstract class ApplicationSingleSelectFragment extends BaseDialogFragment<ApplicationInfo, SingleChoiceListPreference> {

    ApplicationSingleSelectFragment(final String key) {
        super(key);
        setClickOnItemDismissEnabled(true);
    }

    @Override
    protected ListAdapter createListAdapter(final Context context, final List<ApplicationInfo> currentItems) {
        return PreferenceListItemAdapter.singleSelect(context, currentItems);
    }

    @Override
    public void onDialogClosed(final boolean positiveResult) {
        if (positiveResult && listSelections.hasOneSelection()) {
            val selected = listSelections.getSelection();
            getPreference().storeNewSelection(selected.getValue());
        }
    }
}