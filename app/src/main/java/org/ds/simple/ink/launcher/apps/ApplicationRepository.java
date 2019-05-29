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

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import lombok.NonNull;
import lombok.Synchronized;
import lombok.val;
import lombok.var;

import static android.content.Intent.ACTION_MAIN;
import static android.content.Intent.CATEGORY_LAUNCHER;

public final class ApplicationRepository implements IconsThemeRepository.OnIconsThemeLoadedListener {

    public interface OnCacheUpdateListener {

        /**
         * Called right after application cache is reloaded, e.g.
         * when new application is installed or existing one removed.
         */
        void cacheUpdated(final List<ApplicationInfo> newEntries);
    }

    /**
     * Transforms {@link ResolveInfo} into a minimal representation of installed
     * application, which will be shown later on application grid, preference
     * dialogs, etc.
     */
    private final ToApplicationInfo toApplicationInfo = new ToApplicationInfo();

    /**
     * Allows to filter activities for which 'android:exported' is set to 'true',
     * which means the activity will be possible to launch from the application drawer.
     */
    private final ExportedActivitiesOnly exportedActivitiesOnly = new ExportedActivitiesOnly();

    /**
     * Holds a list of objects which subscribed to application cache update event.
     * As long as there is another strong reference to a listener it will stay
     * registered or will be automatically removed from the list otherwise.
     */
    private final Map<OnCacheUpdateListener, Object> listeners = new WeakHashMap<>();

    private final PackageManager packageManager;

    /**
     * By default, applications icons theme is not initialized,
     * so all the icon drawables will be loaded from {@link ResolveInfo}.
     * @see ResolveInfo#loadIcon(PackageManager)
     */
    private IconsTheme iconsTheme = IconsTheme.NOT_INITIALIZED;

    /**
     * The cache will be initialized on the first call to {@link #listAll(boolean)}.
     * Defined as soft reference, so it could be purged by GC if there is such a need.
     */
    private SoftReference<List<ResolveInfo>> resolveInfosCacheRef = new SoftReference<>(null);

    private ApplicationRepository(@NonNull final PackageManager packageManager) {
        this.packageManager = packageManager;
    }

    public void setIconsTheme(@NonNull final IconsTheme iconsTheme) {
        this.iconsTheme = iconsTheme;
    }

    /**
     * Returns information about available/launchable applications.
     * The information is initialized once and retrieved from cache later, unless forced not to do so.
     * Defined as synchronized because it could be called from outside of main thread (via async task).
     * @param forceNotFromCache if true, will reinitialize app cache and return newly generated values
     */
    @Synchronized
    public List<ApplicationInfo> listAll(final boolean forceNotFromCache) {
        var resolveInfos = resolveInfosCacheRef.get();
        if (resolveInfos == null || forceNotFromCache) {

            if (forceNotFromCache) {
                // Removes reference to a previously cached list,
                // so it could be handled by GC.
                resolveInfosCacheRef.clear();
            }

            val intent = new Intent(ACTION_MAIN, null);
            intent.addCategory(CATEGORY_LAUNCHER);

            resolveInfos = packageManager.queryIntentActivities(intent, 0);
            resolveInfosCacheRef = new SoftReference<>(resolveInfos);
        }
        return FluentIterable.from(resolveInfos)
                .filter(exportedActivitiesOnly)
                .transform(toApplicationInfo)
                .copyInto(new ArrayList<>());
    }

    /**
     * Starts up an asynchronous task which reloads application cache and notifies registered listeners about it.
     */
    public void notifyAboutApplicationEvent() {
        new ReloadCacheTask(this).execute();
    }

    /**
     * Registers a listener which will be notified when application cache is reloaded.
     */
    @SuppressWarnings("ConstantConditions")
    public void registerCacheUpdateListener(@NonNull final OnCacheUpdateListener listener) {
        listeners.put(listener, null);
    }

    @Override
    public void iconsThemeLoaded(final IconsTheme newIconsTheme) {
        this.iconsTheme = newIconsTheme;
        notifyAboutApplicationEvent();
    }

    public static ApplicationRepository from(@NonNull final Context context) {
        return new ApplicationRepository(context.getPackageManager());
    }

    private class ExportedActivitiesOnly implements Predicate<ResolveInfo> {

        @Override
        public boolean apply(@NonNull final ResolveInfo resolveInfo) {
            return resolveInfo.activityInfo.exported;
        }
    }

    private class ToApplicationInfo implements Function<ResolveInfo, ApplicationInfo> {

        @Override
        public ApplicationInfo apply(@NonNull final ResolveInfo resolveInfo) {
            val componentName = getComponentName(resolveInfo.activityInfo);
            return ApplicationInfo.builder()
                    .componentName(componentName)
                    .icon(getIcon(componentName, resolveInfo))
                    .label(resolveInfo.loadLabel(packageManager))
                    .build();
        }

        private ComponentName getComponentName(final ActivityInfo activityInfo) {
            return new ComponentName(activityInfo.applicationInfo.packageName, activityInfo.name);
        }

        private Drawable getIcon(final ComponentName componentName, final ResolveInfo resolveInfo) {
            return iconsTheme.getDrawable(componentName.toString(), () -> resolveInfo.loadIcon(packageManager));
        }
    }

    private static class ReloadCacheTask extends AsyncTask<Void, Void, List<ApplicationInfo>> {

        private final WeakReference<ApplicationRepository> applicationRepositoryRef;

        private ReloadCacheTask(final ApplicationRepository applicationRepository) {
            this.applicationRepositoryRef = new WeakReference<>(applicationRepository);
        }

        @Override
        protected List<ApplicationInfo> doInBackground(final Void... voids) {
            val applicationRepository = applicationRepositoryRef.get();
            return (applicationRepository != null) ? applicationRepository.listAll(true) : null;
        }

        @Override
        protected void onPostExecute(final List<ApplicationInfo> newEntries) {
            val applicationRepository = applicationRepositoryRef.get();
            if (applicationRepository != null && newEntries != null) {
                for (val listener : applicationRepository.listeners.keySet()) {
                    listener.cacheUpdated(newEntries);
                }
            }
        }
    }
}