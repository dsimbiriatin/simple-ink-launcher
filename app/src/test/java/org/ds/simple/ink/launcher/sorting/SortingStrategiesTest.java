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

import org.ds.simple.ink.launcher.sorting.strategy.NameInAscOrder;
import org.ds.simple.ink.launcher.sorting.strategy.NameInDescOrder;
import org.ds.simple.ink.launcher.sorting.strategy.ReaderApplicationFirst;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import lombok.val;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

@RunWith(JUnit4.class)
public class SortingStrategiesTest {

    private final SortingStrategies sortingStrategies = new SortingStrategies(() -> "com.abc/MainActivity");

    @Test
    public void shouldReturnValidStrategyImplementationForName() {
        // when
        val nameAscStrategy = sortingStrategies.forName(SortingStrategyName.A_Z);
        assertThat(nameAscStrategy, is(instanceOf(NameInAscOrder.class)));

        // when
        val nameDescStrategy = sortingStrategies.forName(SortingStrategyName.Z_A);
        assertThat(nameDescStrategy, is(instanceOf(NameInDescOrder.class)));

        // when
        val readerAppFirstStrategy = sortingStrategies.forName(SortingStrategyName.READER_APP_FIRST);
        assertThat(readerAppFirstStrategy, is(instanceOf(ReaderApplicationFirst.class)));
    }
}