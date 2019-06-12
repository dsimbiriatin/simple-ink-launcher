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

import org.ds.simple.ink.launcher.R;
import org.ds.simple.ink.launcher.common.BaseListAdapter;

import java.util.List;

import lombok.NonNull;

public class PreferenceListItemAdapter<T extends PreferenceListItem> extends BaseListAdapter<PreferenceListItemView, T> {

    private PreferenceListItemAdapter(final Context context, final int itemViewId, final List<T> items) {
        super(context, itemViewId, items);
    }

    @Override
    protected PreferenceListItemView getView(final PreferenceListItemView itemView, final PreferenceListItem item) {
        itemView.setIcon(item.getIcon());
        itemView.setLabel(item.getLabel());
        return itemView;
    }

    public static <T extends PreferenceListItem> PreferenceListItemAdapter<T> multiSelect(@NonNull final Context context, @NonNull final List<T> items) {
        return new PreferenceListItemAdapter<>(context, R.layout.multi_select_list_item, items);
    }

    public static <T extends PreferenceListItem> PreferenceListItemAdapter<T> singleSelect(@NonNull final Context context, @NonNull final List<T> items) {
        return new PreferenceListItemAdapter<>(context, R.layout.single_select_list_item, items);
    }
}