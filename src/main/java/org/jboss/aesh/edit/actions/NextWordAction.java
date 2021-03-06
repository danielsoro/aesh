/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.aesh.edit.actions;

import org.jboss.aesh.edit.Mode;

/**
 * @author Ståle W. Pedersen <stale.pedersen@jboss.org>
 */
public class NextWordAction extends EditAction {

    private final Mode mode;
    private boolean removeTrailingSpaces = true;

    public NextWordAction(int start, Action action, Mode mode) {
        super(start, action);
        this.mode = mode;
        if(getAction() == Action.CHANGE)
            removeTrailingSpaces = false;
    }

    @Override
    public void doAction(String buffer) {
        int cursor = getStart();

        //if cursor stand on a delimiter, move till its no more delimiters
        if(mode == Mode.EMACS) {
            while (cursor < buffer.length() && (isDelimiter(buffer.charAt(cursor))))
                cursor++;
            while (cursor < buffer.length() && !isDelimiter(buffer.charAt(cursor)))
                cursor++;
        }
        //vi mode
        else {
            if(cursor < buffer.length() && (isDelimiter(buffer.charAt(cursor))))
                while(cursor < buffer.length() && (isDelimiter(buffer.charAt(cursor))))
                    cursor++;
                //if we stand on a non-delimiter
            else {
                while(cursor < buffer.length() && !isDelimiter(buffer.charAt(cursor)))
                    cursor++;
                //if we end up on a space we move past that too
                if(removeTrailingSpaces)
                    if(cursor < buffer.length() && isSpace(buffer.charAt(cursor)))
                        while(cursor < buffer.length() && isSpace(buffer.charAt(cursor)))
                            cursor++;
            }

        }

        //if we end up on a space we move past that too
        if(removeTrailingSpaces)
            if(cursor < buffer.length() && isSpace(buffer.charAt(cursor)))
                while(cursor < buffer.length() && isSpace(buffer.charAt(cursor)))
                    cursor++;

        setEnd(cursor);
    }
}
