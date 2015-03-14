/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.silverspoon.camel.gpio;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.silverspoon.bulldog.core.platform.Board;
import io.silverspoon.bulldog.core.platform.Platform;

/**
 * Represents a GPIO endpoint.
 *
 * @author Pavel Macík <pavel.macik@gmail.com>
 */
public class GpioEndpoint extends DefaultEndpoint {
   public static final String URI_PATTERN_STRING = "gpio://(\\w+)(\\?[\\w=&]+)?";
   public static final Pattern URI_PATTERN = Pattern.compile(URI_PATTERN_STRING);

   private static final Logger log = LoggerFactory.getLogger(GpioEndpoint.class);

   private final String pinName;

   private final Board board;

   private String value = null;

   private long pulseInMicroseconds = 0L;

   public GpioEndpoint(String uri, GpioComponent component) {
      super(uri, component);
      final Matcher m = URI_PATTERN.matcher(uri);
      if (m.matches()) {
         pinName = m.group(1).toUpperCase();
      } else {
         throw new RuntimeException("Specified URI (" + uri + ") does not match the requested pattern (" + URI_PATTERN_STRING + ")");
      }

      board = Platform.createBoard();
      if (log.isInfoEnabled()) {
         log.info("Board found: " + board.getName());
      }
   }

   public Producer createProducer() throws Exception {
      return new GpioProducer(this);
   }

   public Consumer createConsumer(Processor processor) throws Exception {
      return new GpioConsumer(this, processor);
   }

   public boolean isSingleton() {
      return true;
   }

   protected String getPinName() {
      return this.pinName;
   }

   protected Board getBoard() {
      return this.board;
   }

   protected String getValue() {
      return this.value;
   }

   public void setValue(String value) {
      this.value = value;
   }

   public long getPulseInMicroseconds() {
      return pulseInMicroseconds;
   }

   public void setPulseInMicroseconds(final long pulseInMicroseconds) {
      this.pulseInMicroseconds = pulseInMicroseconds;
   }
}
