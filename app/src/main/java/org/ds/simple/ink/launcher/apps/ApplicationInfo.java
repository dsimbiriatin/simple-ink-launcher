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
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

@Value
@Builder
@ToString(of = "componentName")
@EqualsAndHashCode(of = "componentName")
public class ApplicationInfo {

    String label;
    Drawable icon;
    ComponentName componentName;

    public String getFlattenName() {
        return componentName.flattenToString();
    }

    @SuppressWarnings("unused")
    public static class ApplicationInfoBuilder {

        private String label;
        private ComponentName componentName;

        public ApplicationInfoBuilder label(@NonNull final CharSequence label) {
            this.label = label.toString();
            return this;
        }

        public ApplicationInfoBuilder componentName(@NonNull final ComponentName componentName) {
            this.componentName = componentName;
            return this;
        }

        ApplicationInfoBuilder componentName(@NonNull final ActivityInfo activity) {
            this.componentName = new ComponentName(activity.applicationInfo.packageName, activity.name);
            return this;
        }
    }
}