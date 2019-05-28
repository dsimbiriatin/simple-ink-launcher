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

package org.ds.simple.ink.launcher.utils;

import android.content.ComponentName;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import lombok.val;

import static com.google.common.base.Strings.nullToEmpty;

public final class ComponentNameUtils {

    /**
     * Dummy implementation of component name to support null-safe behaviour of util methods.
     */
    public static final ComponentName DUMMY_COMPONENT = new ComponentName("", "");

    /**
     * Used to separate package name from class name in string representation of {@link ComponentName}.
     */
    private static final String COMPONENT_NAME_SEPARATOR = "/";

    private ComponentNameUtils() {
        // do not instantiate
    }

    /**
     * Extracts package name from flatten representation of component name.
     *
     * @see ComponentName#flattenToString()
     */
    @NonNull
    public static String packageNameFrom(@Nullable final String flattenComponentName) {
        val parts = nullToEmpty(flattenComponentName).split(COMPONENT_NAME_SEPARATOR);
        return (parts.length > 0) ? parts[0] : "";
    }

    /**
     * Creates new component name object from its flatten string representation.
     *
     * @see ComponentName#flattenToString()
     */
    @NonNull
    public static ComponentName fromFlattenString(@Nullable final String flattenComponentName) {
        val parts = nullToEmpty(flattenComponentName).split(COMPONENT_NAME_SEPARATOR);
        return (parts.length == 2) ? new ComponentName(parts[0], parts[1]) : DUMMY_COMPONENT;
    }
}