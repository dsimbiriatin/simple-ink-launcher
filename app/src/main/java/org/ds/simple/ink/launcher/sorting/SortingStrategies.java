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

package org.ds.simple.ink.launcher.sorting;

import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableMap;

import org.ds.simple.ink.launcher.apps.ApplicationInfo;
import org.ds.simple.ink.launcher.sorting.strategy.NameInAscOrder;
import org.ds.simple.ink.launcher.sorting.strategy.NameInDescOrder;
import org.ds.simple.ink.launcher.sorting.strategy.ReaderApplicationFirst;

import java.util.Comparator;
import java.util.Map;

import lombok.NonNull;
import lombok.val;

import static org.ds.simple.ink.launcher.sorting.SortingStrategyName.A_Z;
import static org.ds.simple.ink.launcher.sorting.SortingStrategyName.READER_APP_FIRST;
import static org.ds.simple.ink.launcher.sorting.SortingStrategyName.Z_A;

public class SortingStrategies {

    @FunctionalInterface
    interface StrategySupplier extends Supplier<Comparator<ApplicationInfo>> {}

    private final StrategySupplier defaultStrategy;
    private Map<SortingStrategyName, StrategySupplier> sortingStrategiesByName;

    public SortingStrategies(@NonNull final Supplier<String> readerAppName) {
        this.defaultStrategy = () -> new ReaderApplicationFirst(readerAppName.get());

        sortingStrategiesByName = ImmutableMap.of(
                READER_APP_FIRST, defaultStrategy,
                A_Z, NameInAscOrder::new,
                Z_A, NameInDescOrder::new
        );
    }

    public Comparator<ApplicationInfo> forName(@NonNull final SortingStrategyName name) {
        val supplier = sortingStrategiesByName.get(name);
        return (supplier != null) ? supplier.get() : defaultStrategy.get();
    }
}