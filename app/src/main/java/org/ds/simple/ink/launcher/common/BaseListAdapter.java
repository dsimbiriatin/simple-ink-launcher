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

package org.ds.simple.ink.launcher.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.google.common.collect.ImmutableList;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import lombok.NonNull;
import lombok.val;

public abstract class BaseListAdapter<V extends View, T> extends BaseAdapter {

    protected List<T> items;
    private Comparator<T> comparator;

    private final int itemViewId;
    private final LayoutInflater layoutInflater;

    protected BaseListAdapter(final Context context, final int itemViewId) {
        this(context, itemViewId, ImmutableList.of());
    }

    protected BaseListAdapter(final Context context, final int itemViewId, final List<T> items) {
        this.items = items;
        this.itemViewId = itemViewId;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public long getItemId(final int position) {
        return 0;
    }

    @Override
    public T getItem(final int position) {
        return items.get(position);
    }

    @Override
    public View getView(final int position, final View existingView, final ViewGroup parent) {
        val view = reuseExistingViewIfPossible(existingView, parent);
        return getView(view, getItem(position));
    }

    public void setItems(@NonNull final List<T> items) {
        this.items = items;
        sortItemsIfPossible();
        notifyDataSetChanged();
    }

    public void sort(@NonNull final Comparator<T> comparator) {
        this.comparator = comparator;
        sortItemsIfPossible();
        notifyDataSetChanged();
    }

    protected void onItemsSort() {
        // to be overridden by specific implementations
    }

    private void sortItemsIfPossible() {
        if (comparator != null) {
            Collections.sort(items, comparator);
            onItemsSort();
        }
    }

    protected abstract V getView(final V itemView, final T item);

    @SuppressWarnings("unchecked")
    private V reuseExistingViewIfPossible(final View existing, final ViewGroup parent) {
        return (V) (existing == null ? layoutInflater.inflate(itemViewId, parent, false) : existing);
    }
}