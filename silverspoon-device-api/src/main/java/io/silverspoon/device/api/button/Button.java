/**
 * Copyright (C) 2015 the original author or authors.
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
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
package io.silverspoon.device.api.button;

/**
 * Common interface for Button. Wrapper for library-specific interface.
 * 
 * @author sbunciak
 *
 */
public interface Button {

   /**
    * Remove all listeners assigned to the Button.
    */
   public void clearListeners();

   /**
    * Add a new Button listener.
    * 
    * @param {@link io.silverspoon.device.api.button.ButtonListener}
    */
   public void addListener(ButtonListener listener);
}
