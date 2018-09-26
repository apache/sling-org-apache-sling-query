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
import java.util.List;

import org.apache.sling.query.api.internal.IteratorToIteratorFunction;
import org.apache.sling.query.api.internal.Option;

/**
 * This iterator returns all elements of the input list which are mapped to
 * non-empty values by the input function. Name is inspired by the
 * <a href="http://en.wikipedia.org/wiki/Support_(mathematics)">support of the
 * function</a>.
 */
public class SuppIterator<T> extends AbstractIterator<Option<T>> {

    private final List<Option<T>> input;

    private final Iterator<Option<T>> output;

    private Option<T> outputElement;

    private int currentIndex = 0;

    public SuppIterator(List<Option<T>> input, IteratorToIteratorFunction<T> function) {
        this.input = input;
        this.output = function.apply(new ArgumentResettingIterator<T>(input.iterator()));
    }

    /**
     * The idea behind this method is that index of each element in the input
     * iterator is passed to the function. One or more Option<T> items for each
     * index will be returned. If any Option<T> item in that index set is not empty
     * then the corresponding element in the input will be returned.
     */
    @Override
    protected Option<T> getElement() {
        if (outputElement == null) {
            if (!output.hasNext()) {
                return null;
            }
            outputElement = output.next();
        }

        int outputIndex = outputElement.getArgumentId();
        boolean emptyResponse = outputElement.isEmpty();

        // loop to next index or end of list
        while (outputIndex <= currentIndex && output.hasNext()) {
            if (emptyResponse) {
                emptyResponse = outputElement.isEmpty();
            }
            outputElement = output.next();
            outputIndex = outputElement.getArgumentId();
        }

        if (emptyResponse) {
            if (outputIndex > currentIndex) {
                return Option.empty(currentIndex++);
            }
            return null;
        }

        if (outputIndex <= currentIndex) {
            outputElement = null;
        }
        return input.get(currentIndex++);
    }

}
