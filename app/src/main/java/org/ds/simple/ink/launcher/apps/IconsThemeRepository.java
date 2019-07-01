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

package org.ds.simple.ink.launcher.apps;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.AsyncTask;

import com.google.common.collect.ImmutableList;

import org.ds.simple.ink.launcher.R;
import org.ds.simple.ink.launcher.settings.ApplicationSettings;
import org.ds.simple.ink.launcher.utils.ComponentNameUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.*;

import lombok.NonNull;
import lombok.val;

import static android.content.pm.PackageManager.GET_META_DATA;
import static android.util.Xml.Encoding.UTF_8;
import static org.ds.simple.ink.launcher.utils.Exceptions.execute;

public class IconsThemeRepository implements ApplicationSettings.OnIconsThemeChangeListener {

    public interface OnIconsThemeLoadedListener {

        /**
         * Called after new icon theme with corresponding resources is loaded.
         * (happens when user selects new icon theme in application preferences)
         */
        void iconsThemeLoaded(final IconsTheme newIconsTheme);
    }

    private final List<Intent> supportedTypes = ImmutableList.of(
            new Intent("org.adw.launcher.THEMES"));

    private final ApplicationInfo defaultTheme;
    private final PackageManager packageManager;

    /**
     * Holds a list of objects which subscribed to icons theme load event.
     * As long as there is another strong reference to a listener it will stay
     * registered or will be automatically removed from the list otherwise.
     */
    private final Map<OnIconsThemeLoadedListener, Object> listeners = new WeakHashMap<>();

    private IconsThemeRepository(@NonNull final Resources resources, @NonNull final PackageManager packageManager) {
        this.defaultTheme = defaultTheme(resources);
        this.packageManager = packageManager;
    }

    private ApplicationInfo defaultTheme(final Resources resources) {
        return ApplicationInfo.builder()
                .componentName(ComponentNameUtils.DUMMY_COMPONENT)
                .icon(resources.getDrawable(R.drawable.ic_icons_theme))
                .label(resources.getString(R.string.icons_theme_default_label))
                .build();
    }

    /**
     * Starts up an asynchronous task which loads icons theme and notifies registered listeners about it.
     * @param packageName of icon set which was newly selected
     */
    @Override
    public void iconsThemeChanged(@NonNull final String packageName) {
        new IconsThemeLoadTask(this).execute(packageName);
    }

    /**
     * Returns a list of available/supported applications which represent custom icon sets.
     * @see #supportedTypes for the list fo currently supported icon set types.
     */
    public List<ApplicationInfo> getAvailableThemes() {
        val iconsThemes = new ArrayList<ApplicationInfo>();
        iconsThemes.add(defaultTheme);

        for (val typeIntent : supportedTypes) {
            val resolveInfos = packageManager.queryIntentActivities(typeIntent, GET_META_DATA);
            for (val resolveInfo : resolveInfos) {
                iconsThemes.add(ApplicationInfo.builder()
                        .icon(resolveInfo.loadIcon(packageManager))
                        .label(resolveInfo.loadLabel(packageManager))
                        .componentName(resolveInfo.activityInfo)
                        .build());
            }
        }
        return iconsThemes;
    }

    /**
     * Creates icons theme object from custom icon set.
     * @param packageName of icon set with supported type.
     */
    public IconsTheme loadTheme(@NonNull final String packageName) {
        val resources = loadThemeResources(packageName);
        if (resources == null) {
            return IconsTheme.NOT_INITIALIZED;
        }

        val id = resources.getIdentifier("appfilter", "xml", packageName);
        val parser = (id > 0) ? resources.getXml(id) : loadFromAssetsDirectory(resources);

        if (parser == null) {
            return IconsTheme.NOT_INITIALIZED;
        }

        val iconsTheme = new IconsTheme(packageName, resources);
        for (val item : new AppFilterItems(parser)) {
            iconsTheme.addIconResourceIfNotPresent(item);
        }
        return iconsTheme;
    }

    /**
     * Registers a listener which will be notified when new icons theme is loaded.
     */
    @SuppressWarnings("ConstantConditions")
    public void registerOnIconsThemeLoadListener(@NonNull final OnIconsThemeLoadedListener listener) {
        listeners.put(listener, null);
    }

    public static IconsThemeRepository from(final Context context) {
        return new IconsThemeRepository(context.getResources(), context.getPackageManager());
    }

    private Resources loadThemeResources(final String packageName) {
        return execute(() -> packageManager.getResourcesForApplication(packageName)).orNull();
    }

    private XmlPullParser loadFromAssetsDirectory(final Resources resources) {
        try {
            val appFilterStream = resources.getAssets().open("appfilter.xml");
            val xmlParserFactory = XmlPullParserFactory.newInstance();
            xmlParserFactory.setNamespaceAware(true);

            val parser = xmlParserFactory.newPullParser();
            parser.setInput(appFilterStream, UTF_8.name());
            return parser;
        } catch (XmlPullParserException | IOException ex) {
            return null;
        }
    }

    private static class IconsThemeLoadTask extends AsyncTask<String, Void, IconsTheme> {

        private final WeakReference<IconsThemeRepository> iconsThemeRepositoryRef;

        private IconsThemeLoadTask(final IconsThemeRepository iconsThemeRepository) {
            this.iconsThemeRepositoryRef = new WeakReference<>(iconsThemeRepository);
        }

        @Override
        protected IconsTheme doInBackground(final String... packageName) {
            val iconsThemeRepository = iconsThemeRepositoryRef.get();
            return (iconsThemeRepository != null) ? iconsThemeRepository.loadTheme(packageName[0]): null;
        }

        @Override
        protected void onPostExecute(final IconsTheme newIconsTheme) {
            val iconsThemeRepository = iconsThemeRepositoryRef.get();
            if (iconsThemeRepository != null && newIconsTheme != null) {
                for (val listener : iconsThemeRepository.listeners.keySet()) {
                    listener.iconsThemeLoaded(newIconsTheme);
                }
            }
        }
    }
}