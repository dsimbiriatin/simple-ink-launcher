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

import lombok.Value;
import lombok.val;

public final class Exceptions {

    @FunctionalInterface
    public interface MethodWithThrows<T> {
        T execute() throws Exception;
    }

    private Exceptions() {
        // do not instantiate
    }

    public static <T> ExecutionResult<T> execute(final MethodWithThrows<T> method) {
        try {
            val result = method.execute();
            return new ExecutionResult<>(result, true);
        } catch (Exception ex) {
            return new ExecutionResult<>(null, false);
        }
    }

    @Value
    public static class ExecutionResult<T> {

        private T resultObject;
        private boolean success;

        public T orReturn(final T alternativeValue) {
            return success ? resultObject : alternativeValue;
        }
    }
}