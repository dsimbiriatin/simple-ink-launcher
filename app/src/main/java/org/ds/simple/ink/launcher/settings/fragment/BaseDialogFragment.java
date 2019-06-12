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

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.preference.PreferenceDialogFragmentCompat;

import org.ds.simple.ink.launcher.R;
import org.ds.simple.ink.launcher.settings.preference.LauncherDialogPreference;
import org.ds.simple.ink.launcher.settings.preference.PreferenceListItem;

import java.util.List;

import lombok.NonNull;
import lombok.val;

import static android.content.DialogInterface.BUTTON_POSITIVE;
import static org.ds.simple.ink.launcher.settings.fragment.DialogFragmentSize.resize;

public abstract class BaseDialogFragment<T extends PreferenceListItem, P extends LauncherDialogPreference> extends PreferenceDialogFragmentCompat {

    ListSelections<T> listSelections;
    private boolean clickOnItemDismissEnabled;

    BaseDialogFragment(@NonNull final String key) {
        val bundle = new Bundle(1);
        bundle.putString(ARG_KEY, key);
        setArguments(bundle);
    }

    /**
     * Enables 'dismiss dialog on item select' pattern, which is common for single select dialogs.
     */
    void setClickOnItemDismissEnabled(final boolean clickOnItemDismissEnabled) {
        this.clickOnItemDismissEnabled = clickOnItemDismissEnabled;
    }

    @Override
    @SuppressWarnings("unchecked")
    public final P getPreference() {
        return (P) super.getPreference();
    }

    @Override
    protected View onCreateDialogView(final Context context) {
        val listView = (ListView) super.onCreateDialogView(context);
        this.listSelections = ListSelections.countSelectionsFor(listView);

        if (clickOnItemDismissEnabled) {
            listView.setOnItemClickListener(new DismissOnItemClick(this));
        }

        val currentItems = readCurrentItems(context);
        listView.setAdapter(createListAdapter(context, currentItems));
        restorePreviousSelections(listView, currentItems);
        return listView;
    }

    @Override
    @SuppressLint("InflateParams")
    protected void onPrepareDialogBuilder(final AlertDialog.Builder builder) {
        val layoutInflater = LayoutInflater.from(builder.getContext());
        val titleLayout = layoutInflater.inflate(R.layout.select_dialog_title, null);
        builder.setCustomTitle(titleLayout);

        final TextView titleText = titleLayout.findViewById(R.id.title_text);
        titleText.setText(getPreference().getDialogTitle());

        if (clickOnItemDismissEnabled) {
            // Completely removes 'OK' button from dialog
            builder.setPositiveButton(null, null);
        }
    }

    @Override
    @androidx.annotation.NonNull
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        val dialog = super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(di -> resize(dialog, 0.8f, 0.8f));
        return dialog;
    }

    private static class DismissOnItemClick implements AdapterView.OnItemClickListener {

        private final BaseDialogFragment context;

        private DismissOnItemClick(final BaseDialogFragment context) {
            this.context = context;
        }

        @Override
        public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
            val dialog = context.getDialog();
            if (dialog != null) {
                context.onClick(dialog, BUTTON_POSITIVE);
                dialog.dismiss();
            }
        }
    }

    protected abstract List<T> readCurrentItems(final Context context);
    protected abstract ListAdapter createListAdapter(final Context context, final List<T> currentItems);
    protected abstract void restorePreviousSelections(final ListView listView, final List<T> currentItems);
}