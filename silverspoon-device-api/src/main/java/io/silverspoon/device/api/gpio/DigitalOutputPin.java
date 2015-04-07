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
package io.silverspoon.device.api.gpio;

/**
 * Class representing Digital output on the pin.
 * 
 * @author sbunciak
 *
 */
public interface DigitalOutputPin {

   /**
    * Sets the value of the pin to HIGH.
    */
   public void high();

   /**
    * Sets the value of the pin to LOW.
    */
   public void low();

   /**
    * Toggles value of the pin.
    */
   public void toggle();

   /**
    * Queries if the signal on the pin
    * is currently high.
    *
    * @return true, if the signal is high
    */
   public boolean isHigh();

   /**
    * Queries if the signal on the pin
    * is currently low.
    *
    * @return true, if the signal is low
    */
   public boolean isLow();

   /**
    * @return {@link String} representation of the GPIO pin.
    */
   public String getName();
}
