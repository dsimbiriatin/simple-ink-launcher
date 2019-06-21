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

import com.google.common.base.Optional;
import lombok.val;

public final class Exceptions {

    @FunctionalInterface
    public interface MethodWithThrows<T> {
        T execute() throws Exception;
    }

    private Exceptions() {
        // do not instantiate
    }

    public static <T> Optional<T> execute(final MethodWithThrows<T> method) {
        try {
            val result = method.execute();
            return Optional.of(result);
        } catch (Exception ex) {
            return Optional.absent();
        }
    }
}