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

package org.ds.simple.ink.launcher.sorting.strategy;

import org.ds.simple.ink.launcher.apps.ApplicationInfo;

import java.util.Comparator;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ReaderApplicationFirst implements Comparator<ApplicationInfo> {

    private final String readerAppComponentFlattenName;

    @Override
    public int compare(final ApplicationInfo appInfo1, final ApplicationInfo appInfo2) {
        if (isReaderApp(appInfo1) && !isReaderApp(appInfo2)) {
            return -1;
        }

        if (!isReaderApp(appInfo1) && isReaderApp(appInfo2)) {
            return 1;
        }
        return 0;
    }

    private boolean isReaderApp(final ApplicationInfo appInfo) {
        return appInfo.getFlattenName().equals(readerAppComponentFlattenName);
    }
}