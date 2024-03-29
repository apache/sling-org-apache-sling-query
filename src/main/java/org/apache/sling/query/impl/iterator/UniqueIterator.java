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

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.sling.query.api.internal.Option;

/**
 * Provides an iteration of unique objects. During the iteration process this
 * iterator maintains a {@link HashSet} of previously seen items that will be
 * used as filter to prevent duplicates
 *
 * @param <T> Option type
 */
public class UniqueIterator<T> extends AbstractIterator<Option<T>> {

    private Iterator<Option<T>> iterator;

    private Set<T> seen;

    public UniqueIterator(Iterator<Option<T>> input) {
        this.iterator = input;
        seen = new HashSet<>();
    }

    @Override
    protected Option<T> getElement() {
        if (!iterator.hasNext()) {
            iterator = Collections.emptyIterator();
            seen = null;
            return null;
        }
        Option<T> candidate = iterator.next();
        if (!candidate.isEmpty()) {
            if (!seen.add(candidate.getElement())) {
                return Option.empty(candidate.getArgumentId());
            }
        }
        return candidate;
    }

}
