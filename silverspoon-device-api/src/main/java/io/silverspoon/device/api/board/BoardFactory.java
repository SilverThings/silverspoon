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

import java.util.ServiceLoader;

/**
 * Factory class to load GpioBoard instance. 
 * 
 * @author sbunciak
 *
 */
public class BoardFactory {

   /**
    * Loads GpioBoard implementation from classpath.
    * 
    * @return {@link io.silverspoon.device.api.board.GpioBoard}
    * @throws NoSupportedBoardFoundException if no GpioBoard implementation is found on classpath.
    */
   public static GpioBoard getBoardInstance() throws NoSupportedBoardFoundException {
      for (GpioBoard board : ServiceLoader.load(GpioBoard.class)) {
         return board;
      }
      
      throw new NoSupportedBoardFoundException();
   }
}
