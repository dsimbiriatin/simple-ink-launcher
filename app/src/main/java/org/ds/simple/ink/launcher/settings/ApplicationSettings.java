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

package org.ds.simple.ink.launcher.settings;

import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import com.google.common.collect.ImmutableSet;

import org.ds.simple.ink.launcher.R;
import org.ds.simple.ink.launcher.apps.ApplicationInfo;
import org.ds.simple.ink.launcher.sorting.SortingStrategies;
import org.ds.simple.ink.launcher.sorting.SortingStrategyName;
import org.ds.simple.ink.launcher.utils.ComponentNameUtils;

import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import lombok.NonNull;
import lombok.val;

import static androidx.preference.PreferenceManager.getDefaultSharedPreferences;
import static org.ds.simple.ink.launcher.sorting.SortingStrategyName.READER_APP_FIRST;

public class ApplicationSettings implements SharedPreferences.OnSharedPreferenceChangeListener {

    public interface OnIconsThemeChangeListener {

        /**
         * Called after icon theme selection is changed.
         */
        void iconsThemeChanged(final String packageName);
    }

    public interface OnSortingStrategyChangeListener {

        /**
         * Called after application drawer sorting strategy is changed.
         */
        void sortingStrategyChanged(final Comparator<ApplicationInfo> newStrategy);
    }

    public interface OnHideApplicationsChangeListener {

        /**
         * Called after a list of applications which should not be visible on application drawer is changed.
         */
        void hideApplicationsPreferenceChanged(final Set<String> newComponentFlattenNames);
    }

    private final SortingStrategies sortingStrategies;
    private final SharedPreferences sharedPreferences;

    private final String iconsThemeKey;
    private final String defaultReaderAppKey;
    private final String defaultReaderAppDefault;

    private final String hiddenApplicationsKey;
    private final Set<String> hiddenApplicationsDefault;

    private final String sortingStrategyKey;
    private final String sortingStrategyDefault;

    private final String autoStartReaderAppKey;
    private final boolean autoStartReaderAppDefault;

    private final Map<OnIconsThemeChangeListener, Object> iconsThemeChangeListeners = new WeakHashMap<>();
    private final Map<OnSortingStrategyChangeListener, Object> sortingStrategyListeners = new WeakHashMap<>();
    private final Map<OnHideApplicationsChangeListener, Object> hideApplicationsChangeListeners = new WeakHashMap<>();

    private ApplicationSettings(@NonNull final Resources resources, @NonNull final SharedPreferences sharedPreferences) {
        this.sortingStrategies = new SortingStrategies(this::getDefaultReaderApplication);
        this.sharedPreferences = sharedPreferences;

        this.iconsThemeKey = resources.getString(R.string.icons_theme_key);
        this.defaultReaderAppKey = resources.getString(R.string.default_reader_app_key);
        this.defaultReaderAppDefault = resources.getString(R.string.default_reader_app_default_value);

        this.hiddenApplicationsKey = resources.getString(R.string.hidden_applications_key);
        this.hiddenApplicationsDefault = ImmutableSet.of();

        this.sortingStrategyKey = resources.getString(R.string.sorting_strategy_key);
        this.sortingStrategyDefault = resources.getString(R.string.sorting_strategy_default_value);

        this.autoStartReaderAppKey = resources.getString(R.string.auto_start_reader_app_key);
        this.autoStartReaderAppDefault = resources.getBoolean(R.bool.auto_start_reader_app_default_value);
    }

    public Comparator<ApplicationInfo> getSortingStrategy() {
        return sortingStrategies.forName(getSortingStrategyName());
    }

    public Set<String> getHiddenApplications() {
        return sharedPreferences.getStringSet(hiddenApplicationsKey, hiddenApplicationsDefault);
    }

    public boolean isReaderApplicationAutoStartEnabled() {
        return sharedPreferences.getBoolean(autoStartReaderAppKey, autoStartReaderAppDefault);
    }

    public String getIconsTheme() {
        val flattenComponentName = sharedPreferences.getString(iconsThemeKey, "");
        return ComponentNameUtils.packageNameFrom(flattenComponentName);
    }

    public ComponentName getDefaultReaderApplicationComponentName() {
        val flattenComponentName = getDefaultReaderApplication();
        return ComponentNameUtils.fromFlattenString(flattenComponentName);
    }

    private String getDefaultReaderApplication() {
        return sharedPreferences.getString(defaultReaderAppKey, defaultReaderAppDefault);
    }

    private SortingStrategyName getSortingStrategyName() {
        return SortingStrategyName.valueOf(sharedPreferences.getString(sortingStrategyKey, sortingStrategyDefault));
    }

    @SuppressWarnings("ConstantConditions")
    public void registerIconsThemeChangeListener(@NonNull final OnIconsThemeChangeListener listener) {
        iconsThemeChangeListeners.put(listener, null);
    }

    @SuppressWarnings("ConstantConditions")
    public void registerSortingStrategyChangeListener(@NonNull final OnSortingStrategyChangeListener listener) {
        sortingStrategyListeners.put(listener, null);
    }

    @SuppressWarnings("ConstantConditions")
    public void registerHideApplicationsChangeListener(@NonNull final OnHideApplicationsChangeListener listener) {
        hideApplicationsChangeListeners.put(listener, null);
    }

    public void notifyApplicationRemoved(final String packageName) {
        if (getIconsTheme().equals(packageName)) {
            val edit = sharedPreferences.edit();
            edit.putString(iconsThemeKey, "");
            edit.apply();
        }
    }

    @Override
    public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String key) {
        if (key.equals(iconsThemeKey)) {
            notifyIconsThemeChanged(getIconsTheme());
        }

        if (key.equals(sortingStrategyKey)) {
            notifySortingStrategyChanged(getSortingStrategy());
        }

        if (key.equals(hiddenApplicationsKey)) {
            notifyHideApplicationsChanged(getHiddenApplications());
        }

        if (key.equals(defaultReaderAppKey) && READER_APP_FIRST == getSortingStrategyName()) {
            notifySortingStrategyChanged(getSortingStrategy());
        }
    }

    private void notifyIconsThemeChanged(final String newPackageName) {
        for (val listener : iconsThemeChangeListeners.keySet()) {
            listener.iconsThemeChanged(newPackageName);
        }
    }

    private void notifySortingStrategyChanged(final Comparator<ApplicationInfo> newStrategy) {
        for (val listener : sortingStrategyListeners.keySet()) {
            listener.sortingStrategyChanged(newStrategy);
        }
    }

    private void notifyHideApplicationsChanged(final Set<String> newComponentFlattenNames) {
        for (val listener : hideApplicationsChangeListeners.keySet()) {
            listener.hideApplicationsPreferenceChanged(newComponentFlattenNames);
        }
    }

    public static ApplicationSettings from(@NonNull final Context context) {
        return new ApplicationSettings(context.getResources(), getDefaultSharedPreferences(context));
    }
}