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
package io.silverspoon.device.api.board;

import io.silverspoon.device.api.button.Button;
import io.silverspoon.device.api.gpio.DigitalInputPin;
import io.silverspoon.device.api.gpio.DigitalOutputPin;

/**
 * Interface to provide a common Board API.
 * 
 * @author sbunciak
 *
 */
public interface GpioBoard {

   /**
    * Retrieves (and exports if necessary) GPIO pin as a digital output.   
    * 
    * @param pinName
    * @return {@link DigitalOutputPin}
    */
   public DigitalOutputPin getDigitalOutputPin(String pinName);

   /**
    * Retrieves (and exports if necessary) GPIO pin as a digital input.
    * 
    * @param pinName
    * @return
    */
   public DigitalInputPin getDigitalInputPin(String pinName);

   /**
    * Creates new Button implementation attached to the specified GPIO pin.
    * 
    * @param pin
    * @return
    */
   public Button getButton(DigitalInputPin pin);

   /**
    * @return {@link String} representation of the Board.
    */
   public String getName();

}
