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

package org.ds.simple.ink.launcher.drawer;

import android.content.Context;

import org.ds.simple.ink.launcher.R;
import org.ds.simple.ink.launcher.common.BaseListAdapter;
import org.ds.simple.ink.launcher.apps.ApplicationInfo;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import lombok.NonNull;
import lombok.val;

public class ApplicationDrawerAdapter extends BaseListAdapter<ApplicationDrawerItem, ApplicationInfo> {

    private int itemIconSize;
    private Set<String> currentHiddenIcons = new HashSet<>();
    private SortedSet<Integer> hiddenItemsPositions = new TreeSet<>();

    ApplicationDrawerAdapter(final Context context) {
        super(context, R.layout.app_drawer_item);
    }

    @Override
    public int getCount() {
        return super.getCount() - hiddenItemsPositions.size();
    }

    @Override
    public ApplicationInfo getItem(final int position) {
        val actualPosition = getActualPosition(position);
        return items.get(actualPosition);
    }

    @Override
    protected ApplicationDrawerItem getView(final ApplicationDrawerItem itemView, final ApplicationInfo item) {
        itemView.updateWith(item, itemIconSize);
        return itemView;
    }

    @Override
    protected void onItemsSort() {
        calculateHiddenItemsPositions(currentHiddenIcons);
    }

    void setItemIconSize(final int size) {
        itemIconSize = size;
        notifyDataSetChanged();
    }

    void hideItems(@NonNull final Set<String> componentFlattenNames) {
        currentHiddenIcons = componentFlattenNames;
        calculateHiddenItemsPositions(currentHiddenIcons);
        notifyDataSetChanged();
    }

    private void calculateHiddenItemsPositions(final Set<String> hiddenItems) {
        hiddenItemsPositions.clear();
        for (int i = 0; i < items.size(); ++i) {
            val applicationInfo = items.get(i);
            if (hiddenItems.contains(applicationInfo.getFlattenName())) {
                hiddenItemsPositions.add(i);
            }
        }
    }

    private int getActualPosition(final int position) {
        int actualPosition = position;
        for (val hiddenPosition : hiddenItemsPositions) {
            if (hiddenPosition <= actualPosition) {
                actualPosition++;
            } else {
                break;
            }
        }
        return actualPosition;
    }
}