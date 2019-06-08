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
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import com.google.common.base.Supplier;

import java.util.HashMap;
import java.util.Map;

import lombok.NonNull;
import lombok.val;

import static org.ds.simple.ink.launcher.utils.Exceptions.execute;

public class IconsTheme {

    /**
     * Used to represent a theme which was not initialized yet or failed to be initialized.
     * When try to get a drawable from it, a default drawable will be returned back instead.
     * @see #getDrawable(String, Supplier)
     */
    static final IconsTheme NOT_INITIALIZED = new IconsTheme("non_initialized", null);

    private final String packageName;
    private final Resources iconResources;

    /**
     * Contains a mapping between {@link ComponentName#toString()} and {@link Drawable} name.
     * This cache is used later to identify drawable resource in {@link #iconResources}.
     */
    private final Map<String, String> resourcesMapping = new HashMap<>();

    IconsTheme(@NonNull final String packageName, final Resources iconResources) {
        this.packageName = packageName;
        this.iconResources = iconResources;
    }

    /**
     * Creates a mapping entry for provided "appfilter.xml" item.
     */
    void addIconResourceIfNotPresent(@NonNull final AppFilterItems.Item item) {
        val existing = resourcesMapping.get(item.getComponentName());
        if (existing == null) {
            resourcesMapping.put(item.getComponentName(), item.getDrawableName());
        }
    }

    /**
     * Returns a drawable object associated with provided component.
     * If current theme does not have suitable icon, the default one will be returned instead.
     */
    Drawable getDrawable(@NonNull final String componentName, @NonNull final Supplier<Drawable> defaultDrawable) {
        if (isNotInitialized()) {
            return defaultDrawable.get();
        }
        val drawableName = resourcesMapping.get(componentName);
        if (drawableName == null) {
            return defaultDrawable.get();
        }
        val loadedDrawable = loadDrawable(drawableName);
        if (loadedDrawable == null) {
            return defaultDrawable.get();
        }
        return loadedDrawable;
    }

    private Drawable loadDrawable(final String drawableName) {
        val id = iconResources.getIdentifier(drawableName, "drawable", packageName);
        return execute(() -> iconResources.getDrawable(id)).orReturn(null);
    }

    private boolean isNotInitialized() {
        return NOT_INITIALIZED.packageName.equals(packageName);
    }
}