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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

import org.ds.simple.ink.launcher.BaseLauncherActivity;
import org.ds.simple.ink.launcher.LauncherSettingsActivity;
import org.ds.simple.ink.launcher.apps.ApplicationInfo;
import org.ds.simple.ink.launcher.apps.ApplicationRepository;
import org.ds.simple.ink.launcher.common.ActivityTypeAware;
import org.ds.simple.ink.launcher.settings.ApplicationSettings;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.content.Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED;

public class ApplicationDrawer extends GridView implements ActivityTypeAware,
                                                           ApplicationRepository.OnCacheUpdateListener,
                                                           ApplicationSettings.OnSortingStrategyChangeListener,
                                                           ApplicationSettings.OnHideApplicationsChangeListener,
                                                           ApplicationSettings.OnMainScreenSettingsChangeListener {
    public interface OnTotalItemCountChangeListener {

        /**
         * Called when total number of visible applications on the grid changes.
         * @see #setApplications(List)
         * @see #hideApplications(Set)
         */
        void totalCountChanged(final int newCount);
    }

    /**
     * Holds a list of objects which subscribed to applications total count changes.
     * As long as there is another strong reference to a listener it will stay
     * registered or will be automatically removed from the list otherwise.
     */
    private final Map<OnTotalItemCountChangeListener, Object> listeners = new WeakHashMap<>();

    /**
     * Detects long press touch events, which occur on grid's space, not occupied by any item.
     * Currently used to invoke launcher settings upon this event.
     * @see LongPressOnEmptyGridSpace
     */
    private GestureDetector longPressOnEmptySpaceDetector;

    public ApplicationDrawer(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        setOnItemClickListener(new StartSelectedActivity());
        setAdapter(new ApplicationDrawerAdapter(getContext()));

        val activity = getActivity(context, BaseLauncherActivity.class);
        enableActionModeSupportForGridItems(activity);
        enableActionsOnGridEmptySpaceLongPress(activity);
    }

    private void enableActionModeSupportForGridItems(final BaseLauncherActivity activity) {
        setMultiChoiceModeListener(new ItemActionModeListener(activity, getAdapter()::getItem));
        setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
    }

    private void enableActionsOnGridEmptySpaceLongPress(final BaseLauncherActivity activity) {
        longPressOnEmptySpaceDetector = new GestureDetector(activity, new LongPressOnEmptyGridSpace(activity));
    }

    public void setApplications(@NonNull final List<ApplicationInfo> applicationInfos) {
        getAdapter().setItems(applicationInfos);
    }

    public void sortApplications(@NonNull final Comparator<ApplicationInfo> comparator) {
        getAdapter().sort(comparator);
    }

    public void hideApplications(@NonNull final Set<String> componentFlattenNames) {
        getAdapter().hideItems(componentFlattenNames);
    }

    public void applyMainScreenPreferences(@NonNull final MainScreenPreferences preferences) {
        val density = getResources().getDisplayMetrics().density;
        getAdapter().setItemIconSize((int) (preferences.getIconSize() * density));
        setNumColumns(preferences.getColumns());
    }

    @Override
    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouchEvent(final MotionEvent event) {
        return longPressOnEmptySpaceDetector.onTouchEvent(event) || super.onTouchEvent(event);
    }

    @Override
    public int getCount() {
        return getAdapter().getCount();
    }

    @Override
    public ApplicationDrawerAdapter getAdapter() {
        return (ApplicationDrawerAdapter) super.getAdapter();
    }

    @Override
    public void cacheUpdated(final List<ApplicationInfo> newEntries) {
        setApplications(newEntries);
        notifyTotalCountChanged(getCount());
    }

    @Override
    public void sortingStrategyChanged(final Comparator<ApplicationInfo> newStrategy) {
        sortApplications(newStrategy);
    }

    @Override
    public void hideApplicationsPreferenceChanged(final Set<String> newComponentFlattenNames) {
        hideApplications(newComponentFlattenNames);
        notifyTotalCountChanged(getCount());
    }

    @Override
    public void mainScreenPreferencesChanged(final MainScreenPreferences newPreferences) {
        applyMainScreenPreferences(newPreferences);
    }

    @SuppressWarnings("ConstantConditions")
    public void registerOnTotalCountChangeListener(@NonNull final OnTotalItemCountChangeListener listener) {
        listeners.put(listener, null);
    }

    private void notifyTotalCountChanged(final int newCount) {
        for (val listener : listeners.keySet()) {
            listener.totalCountChanged(newCount);
        }
    }

    @RequiredArgsConstructor
    private class LongPressOnEmptyGridSpace extends GestureDetector.SimpleOnGestureListener {

        private final Activity activity;

        @Override
        public void onLongPress(final MotionEvent event) {
            if (isNotPressedOnGridItem(event)) {
                val settingsIntent = new Intent(activity, LauncherSettingsActivity.class);
                activity.startActivity(settingsIntent);
            }
        }

        private boolean isNotPressedOnGridItem(final MotionEvent event) {
            return pointToPosition((int) event.getX(), (int) event.getY()) == -1;
        }
    }

    private static class StartSelectedActivity implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
            val applicationInfo = (ApplicationInfo) parent.getItemAtPosition(position);
            val intent = Intent.makeMainActivity(applicationInfo.getComponentName());
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            view.getContext().startActivity(intent);
        }
    }
}