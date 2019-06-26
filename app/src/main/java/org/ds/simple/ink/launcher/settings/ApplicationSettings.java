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
import org.ds.simple.ink.launcher.settings.ApplicationSettings.OnMainScreenSettingsChangeListener.MainScreenPreferences;
import org.ds.simple.ink.launcher.sorting.SortingStrategies;
import org.ds.simple.ink.launcher.sorting.SortingStrategyName;
import org.ds.simple.ink.launcher.toolbar.ToolbarLocationName;
import org.ds.simple.ink.launcher.utils.ComponentNameUtils;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import lombok.NonNull;
import lombok.Value;
import lombok.val;

import static androidx.preference.PreferenceManager.getDefaultSharedPreferences;
import static com.google.common.collect.Sets.newHashSet;
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

    public interface OnWifiSwitchEnabledChangeListener {

        /**
         * Called after toolbar's wifi switch is enabled or disabled.
         */
        void wifiSwitchEnabled(final boolean whetherEnabled);
    }

    public interface OnBacklightSwitchEnabledChangeListener {


        /**
         * Called after toolbar's backlight switch is enabled or disabled.
         */
        void backlightSwitchEnabled(final boolean whetherEnabled);
    }

    public interface OnToolbarLocationChangeListener {

        /**
         * Called after toolbar changes its location from top <-> bottom.
         */
        void toolbarLocationChanged(final ToolbarLocationName newLocation);
    }

    public interface OnMainScreenSettingsChangeListener {

        /**
         * Called after application grid attributes have changed.
         */
        void mainScreenPreferencesChanged(final MainScreenPreferences newPreferences);

        @Value
        class MainScreenPreferences {
            private int columns;
            private int iconSize;
        }
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

    private final String showWifiSwitchKey;
    private final boolean showWifiSwitchDefault;

    private final String showBacklightSwitchKey;
    private final boolean showBacklightSwitchDefault;

    private final String toolbarLocationKey;
    private final String toolbarLocationDefault;

    private final String mainScreenColumnsKey;
    private final int mainScreenColumnsDefault;

    private final String mainScreenIconsSizeKey;
    private final int mainScreenIconsSizeDefault;

    private final Set<String> mainScreenPreferenceKeys = new HashSet<>();

    private final Map<OnIconsThemeChangeListener, Object> iconsThemeChangeListeners = new WeakHashMap<>();
    private final Map<OnSortingStrategyChangeListener, Object> sortingStrategyListeners = new WeakHashMap<>();
    private final Map<OnToolbarLocationChangeListener, Object> toolbarLocationChangeListeners = new WeakHashMap<>();
    private final Map<OnHideApplicationsChangeListener, Object> hideApplicationsChangeListeners = new WeakHashMap<>();
    private final Map<OnWifiSwitchEnabledChangeListener, Object> wifiSwitchEnabledChangeListeners = new WeakHashMap<>();
    private final Map<OnMainScreenSettingsChangeListener, Object> mainScreenSettingsChangeListeners = new WeakHashMap<>();
    private final Map<OnBacklightSwitchEnabledChangeListener, Object> backlightSwitchEnabledChangeListeners = new WeakHashMap<>();

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

        this.showWifiSwitchKey = resources.getString(R.string.show_wifi_switch_key);
        this.showWifiSwitchDefault = resources.getBoolean(R.bool.show_wifi_switch_default_value);

        this.showBacklightSwitchKey = resources.getString(R.string.show_backlight_switch_key);
        this.showBacklightSwitchDefault = resources.getBoolean(R.bool.show_backlight_switch_default_value);

        this.toolbarLocationKey = resources.getString(R.string.toolbar_location_key);
        this.toolbarLocationDefault = resources.getString(R.string.toolbar_location_default_value);

        this.mainScreenColumnsKey = resources.getString(R.string.main_screen_columns_key);
        this.mainScreenColumnsDefault = resources.getInteger(R.integer.main_screen_columns_default_value);

        this.mainScreenIconsSizeKey = resources.getString(R.string.main_screen_icons_size_key);
        this.mainScreenIconsSizeDefault = resources.getInteger(R.integer.main_screen_icons_size_default_value);

        mainScreenPreferenceKeys.add(mainScreenColumnsKey);
        mainScreenPreferenceKeys.add(mainScreenIconsSizeKey);
    }

    public Comparator<ApplicationInfo> getSortingStrategy() {
        return sortingStrategies.forName(getSortingStrategyName());
    }

    public Set<String> getHiddenApplications() {
        return sharedPreferences.getStringSet(hiddenApplicationsKey, hiddenApplicationsDefault);
    }

    public boolean showWifiSwitch() {
        return sharedPreferences.getBoolean(showWifiSwitchKey, showWifiSwitchDefault);
    }

    public boolean showBacklightSwitch() {
        return sharedPreferences.getBoolean(showBacklightSwitchKey, showBacklightSwitchDefault);
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

    public ToolbarLocationName getToolbarLocation() {
        val toolbarLocation = sharedPreferences.getString(toolbarLocationKey, toolbarLocationDefault);
        return ToolbarLocationName.valueOf(toolbarLocation);
    }

    public MainScreenPreferences getMainScreenPreferences() {
        val columns = sharedPreferences.getInt(mainScreenColumnsKey, mainScreenColumnsDefault);
        val iconsSize = sharedPreferences.getInt(mainScreenIconsSizeKey, mainScreenIconsSizeDefault);
        return new MainScreenPreferences(columns, iconsSize);
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
    public void registerWifiSwitchEnabledChangeListener(@NonNull final OnWifiSwitchEnabledChangeListener listener) {
        wifiSwitchEnabledChangeListeners.put(listener, null);
    }

    @SuppressWarnings("ConstantConditions")
    public void registerBacklightSwitchEnabledChangeListener(@NonNull final OnBacklightSwitchEnabledChangeListener listener) {
        backlightSwitchEnabledChangeListeners.put(listener, null);
    }

    @SuppressWarnings("ConstantConditions")
    public void registerHideApplicationsChangeListener(@NonNull final OnHideApplicationsChangeListener listener) {
        hideApplicationsChangeListeners.put(listener, null);
    }

    @SuppressWarnings("ConstantConditions")
    public void registerToolbarLocationChangeListener(@NonNull final OnToolbarLocationChangeListener listener) {
        toolbarLocationChangeListeners.put(listener, null);
    }

    @SuppressWarnings("ConstantConditions")
    public void registerMainScreenPreferencesChangeListener(@NonNull final OnMainScreenSettingsChangeListener listener) {
        mainScreenSettingsChangeListeners.put(listener, null);
    }

    public void notifyApplicationRemoved(final String packageName) {
        if (getIconsTheme().equals(packageName)) {
            val edit = sharedPreferences.edit();
            edit.putString(iconsThemeKey, "");
            edit.apply();
        }
    }

    public void notifyNewHiddenApplications(@NonNull final Set<String> flattenComponentNames) {
        val hiddenApplications = newHashSet(getHiddenApplications());
        hiddenApplications.addAll(flattenComponentNames);

        val edit = sharedPreferences.edit();
        edit.putStringSet(hiddenApplicationsKey, hiddenApplications);
        edit.apply();
    }

    @Override
    public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String key) {
        if (key.equals(iconsThemeKey)) {
            notifyIconsThemeChanged(getIconsTheme());
        }

        if (key.equals(toolbarLocationKey)) {
            notifyToolbarLocationChanged(getToolbarLocation());
        }

        if (key.equals(showWifiSwitchKey)) {
            notifyWifiSwitchEnabledChanged(showWifiSwitch());
        }

        if (key.equals(sortingStrategyKey)) {
            notifySortingStrategyChanged(getSortingStrategy());
        }

        if (key.equals(hiddenApplicationsKey)) {
            notifyHideApplicationsChanged(getHiddenApplications());
        }

        if (key.equals(showBacklightSwitchKey)) {
            notifyBacklightSwitchEnabledChanged(showBacklightSwitch());
        }

        if (mainScreenPreferenceKeys.contains(key)) {
            notifyMainScreenPreferencesChanged(getMainScreenPreferences());
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

    private void notifyWifiSwitchEnabledChanged(final boolean whetherEnabled) {
        for (val listener : wifiSwitchEnabledChangeListeners.keySet()) {
            listener.wifiSwitchEnabled(whetherEnabled);
        }
    }

    private void notifyBacklightSwitchEnabledChanged(final boolean whetherEnabled) {
        for (val listener : backlightSwitchEnabledChangeListeners.keySet()) {
            listener.backlightSwitchEnabled(whetherEnabled);
        }
    }

    private void notifyToolbarLocationChanged(final ToolbarLocationName newLocation) {
        for (val listener : toolbarLocationChangeListeners.keySet()) {
            listener.toolbarLocationChanged(newLocation);
        }
    }

    private void notifyMainScreenPreferencesChanged(final MainScreenPreferences newPreferences) {
        for (val listener : mainScreenSettingsChangeListeners.keySet()) {
            listener.mainScreenPreferencesChanged(newPreferences);
        }
    }

    public static ApplicationSettings from(@NonNull final Context context) {
        return new ApplicationSettings(context.getResources(), getDefaultSharedPreferences(context));
    }
}