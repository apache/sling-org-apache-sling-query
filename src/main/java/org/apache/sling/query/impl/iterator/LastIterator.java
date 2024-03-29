/*-
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.sling.query.impl.iterator;

import java.util.Iterator;

import org.apache.sling.query.api.internal.Option;

/**
 *
 *
 *
 * @param <T> Option type
 */
public class LastIterator<T> extends AbstractIterator<Option<T>> {

    private final Iterator<Option<T>> iterator;

    private Option<T> previous;

    public LastIterator(Iterator<Option<T>> iterator) {
        this.iterator = iterator;
    }

    @Override
    protected Option<T> getElement() {
        Option<T> candidate = previous;
        if (!iterator.hasNext()) {
            previous = null;
            return candidate;
        }
        if (candidate == null) {
            candidate = iterator.next();
        }
        while (candidate.isEmpty() && iterator.hasNext()) {
            candidate = iterator.next();
        }
        if (!iterator.hasNext()) {
            return candidate;
        }
        Option<T> next = iterator.next();
        while (next.isEmpty() && iterator.hasNext()) {
            next = iterator.next();
        }
        if (!iterator.hasNext() && next.isEmpty()) {
            return candidate;
        }
        previous = next;
        return Option.empty(candidate.getArgumentId());
    }

}
