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

package org.ds.simple.ink.launcher.common;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;

import lombok.NonNull;
import lombok.val;

public interface ActivityTypeAware {

    default <T extends Activity> T getActivity(@NonNull final Context context, @NonNull final Class<T> type) {
        return unwrap(context, type);
    }

    default <T extends Activity> T unwrap(final Context context, final Class<T> type) {
        if (context instanceof ContextWrapper) {
            val wrapper = (ContextWrapper) context;

            if (type.isInstance(wrapper)) {
                return type.cast(wrapper);
            } else {
                return unwrap(wrapper.getBaseContext(), type);
            }
        }
        return null;
    }
}