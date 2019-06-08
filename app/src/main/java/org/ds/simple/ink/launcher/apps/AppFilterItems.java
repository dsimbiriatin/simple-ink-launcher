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

import org.xmlpull.v1.XmlPullParser;

import java.util.Iterator;

import lombok.Data;
import lombok.NonNull;
import lombok.val;

import static org.ds.simple.ink.launcher.apps.AppFilterItems.*;
import static org.ds.simple.ink.launcher.utils.Exceptions.execute;
import static org.xmlpull.v1.XmlPullParser.END_DOCUMENT;
import static org.xmlpull.v1.XmlPullParser.START_DOCUMENT;
import static org.xmlpull.v1.XmlPullParser.START_TAG;

/**
 * Allows to navigate through "appfilter.xml" in iterative manner.
 * On each {@link #next()} call an item is created, which represents
 * content of next available <item>...</item> tag in the file being parsed.
 */
class AppFilterItems implements Iterable<Item>, Iterator<Item> {

    private final XmlPullParser parser;

    AppFilterItems(@NonNull final XmlPullParser parser) {
        this.parser = parser;
    }

    @Override
    @androidx.annotation.NonNull
    public Iterator<Item> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        int eventType = getEventType();
        if (eventType != START_DOCUMENT) {
            eventType = getNext();
        }

        for (int type = eventType; type != END_DOCUMENT; type = getNext()) {
            if (type == START_TAG && "item".equals(parser.getName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Item next() {
        val item = new Item();
        for (int i = 0; i < parser.getAttributeCount(); ++i) {

            if ("component".equals(parser.getAttributeName(i))) {
                item.setComponentName(parser.getAttributeValue(i));
            }

            if ("drawable".equals(parser.getAttributeName(i))) {
                item.setDrawableName(parser.getAttributeValue(i));
            }
        }
        return item;
    }

    private int getNext() {
        return execute(parser::next).orReturn(-1);
    }

    private int getEventType() {
        return execute(parser::getEventType).orReturn(-1);
    }

    @Data
    static class Item {
        private String drawableName;
        private String componentName;
    }
}