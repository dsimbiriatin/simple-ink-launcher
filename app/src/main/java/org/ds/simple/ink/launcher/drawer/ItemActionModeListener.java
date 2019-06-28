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

import android.content.Intent;
import android.net.Uri;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;

import org.ds.simple.ink.launcher.BaseLauncherActivity;
import org.ds.simple.ink.launcher.R;
import org.ds.simple.ink.launcher.apps.ApplicationInfo;

import java.util.HashSet;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import lombok.val;

import static com.google.common.collect.Iterables.getOnlyElement;

@RequiredArgsConstructor
public class ItemActionModeListener implements AbsListView.MultiChoiceModeListener {

    private final BaseLauncherActivity activity;
    private final Set<ApplicationInfo> selections = new HashSet<>();
    private final Function<Integer, ApplicationInfo> selectionSupplier;

    @Override
    public boolean onCreateActionMode(final ActionMode mode, final Menu menu) {
        mode.getMenuInflater().inflate(R.menu.app_drawer_item_menu, menu);
        return true;
    }

    @Override
    public void onItemCheckedStateChanged(final ActionMode mode, final int position, final long id, final boolean checked) {
        val selection = selectionSupplier.apply(position);
        if (updateSelections(selection, checked)) {
            mode.setTitle("  " + selections.size());
            val deleteItem = mode.getMenu().findItem(R.id.delete_action);
            deleteItem.setVisible(selections.size() <= 1);
        }
    }

    private boolean updateSelections(final ApplicationInfo selection, final boolean checked) {
        return checked ? selections.add(selection) : selections.remove(selection);
    }

    @Override
    public boolean onActionItemClicked(final ActionMode mode, final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.hide_action:
                markSelectionsAsHidden();
                break;
            case R.id.delete_action:
                requestSelectionDelete();
                break;
            default:
                return false;
        }
        mode.finish();
        return true;
    }

    private void markSelectionsAsHidden() {
        val newHiddenApps = FluentIterable.from(selections)
                .transform(ApplicationInfo::getFlattenName)
                .toSet();

        val settings = activity.getApplicationSettings();
        settings.notifyNewHiddenApplications(newHiddenApps);
    }

    private void requestSelectionDelete() {
        val packageName = getOnlyElement(selections)
                .getComponentName()
                .getPackageName();

        val uninstallIntent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE);
        uninstallIntent.setData(Uri.parse("package:" + packageName));
        activity.startActivity(uninstallIntent);
    }

    @Override
    public boolean onPrepareActionMode(final ActionMode mode, final Menu menu) {
        return false;
    }

    @Override
    public void onDestroyActionMode(final ActionMode mode) {
        selections.clear();
    }
}