/**
 * Copyright (c) 2012 sprogcoder <sprogcoder@gmail.com>
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dashroshan.dltrainer.exception;

public class WindowNotFoundException extends Exception {
    private static final long serialVersionUID = -7065188795268185722L;

    public WindowNotFoundException(String className, String windowName) {
        super(String.format("Window Handle null for className: %s; windowName: %s", className, windowName));
    }
}
