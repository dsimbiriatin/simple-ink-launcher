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

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.preference.Preference;

import org.ds.simple.ink.launcher.MainScreenSettingsActivity;
import org.ds.simple.ink.launcher.R;
import org.ds.simple.ink.launcher.settings.preference.MultiChoiceListPreference;
import org.ds.simple.ink.launcher.settings.preference.SingleChoiceListPreference;

import lombok.val;

public class LauncherPreferencesFragment extends BasePreferencesFragment {

    @Override
    public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
        setPreferencesFromResource(R.xml.launcher_preferences, rootKey);
        onPreferenceClickNavigateTo(R.string.main_screen_preferences_key, MainScreenSettingsActivity.class);
    }

    @Override
    public void onDisplayPreferenceDialog(final Preference preference) {
        if (preference instanceof MultiChoiceListPreference) {
            showFragment(new HiddenApplicationsFragment(preference.getKey()));

        } else if (preference instanceof SingleChoiceListPreference) {
            showSingleChoicePreferenceFragment(preference);

        } else {
            super.onDisplayPreferenceDialog(preference);
        }
    }

    private void showSingleChoicePreferenceFragment(final Preference preference) {
        val key = preference.getKey();
        if (key.equals(getString(R.string.default_reader_app_key))) {
            showFragment(new DefaultReaderAppFragment(key));
        } else if (key.equals(getString(R.string.icons_theme_key))) {
            showFragment(new IconsThemeFragment(key));
        } else if (key.equals(getString(R.string.toolbar_location_key))) {
            showFragment(new ToolbarLocationFragment(key));
        } else if (key.equals(getString(R.string.sorting_strategy_key))) {
            showFragment(new AppDrawerSortingFragment(key));
        }
    }

    private void showFragment(final DialogFragment dialogFragment) {
        dialogFragment.setTargetFragment(this, 0);
        dialogFragment.show(requireFragmentManager(), null);
    }
}