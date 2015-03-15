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

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.silverspoon.bulldog.core.gpio.DigitalOutput;
import io.silverspoon.bulldog.core.gpio.Pin;
import io.silverspoon.bulldog.core.util.BulldogUtil;

/**
 * The GPIO producer.
 *
 * @author Pavel Macík <pavel.macik@gmail.com>
 */
public class GpioProducer extends DefaultProducer {
   private static final transient Logger Log = LoggerFactory.getLogger(GpioProducer.class);
   private GpioEndpoint endpoint;
   private final Pin pin;
   private DigitalOutput output;

   public GpioProducer(GpioEndpoint endpoint) {
      super(endpoint);
      this.endpoint = endpoint;

      pin = endpoint.getBoard().getPin(endpoint.getPinName());
      if (log.isInfoEnabled()) {
         log.info("Pin attached: " + pin.getName());
      }

      output = pin.as(DigitalOutput.class);
   }

   public void process(Exchange exchange) throws Exception {
      final String value = endpoint.getValue();
      if (value != null) {
         setPinValue(value);
      } else {
         if (exchange.getIn().getBody().toString().endsWith("1")) {
            setPinValue(GpioComponent.HIGH);
         } else {
            setPinValue(GpioComponent.LOW);
         }
      }
   }

   private void setPinValue(final String value) {
      final long pulseInMicroseconds = this.endpoint.getPulseInMicroseconds();
      switch (value) {
         case GpioComponent.HIGH:
            if (log.isDebugEnabled()) {
               log.debug("Setting pin " + endpoint.getPinName() + " to HIGH state.");
            }
            output.high();
            if (pulseInMicroseconds > 0) {
               if (log.isDebugEnabled()) {
                  log.debug("Waiting for " + pulseInMicroseconds + " microseconds.");
               }
               BulldogUtil.sleepNs(pulseInMicroseconds * 1000L);
               if (log.isDebugEnabled()) {
                  log.debug("Setting pin " + endpoint.getPinName() + " to LOW state.");
               }
               output.low();
            }
            break;
         case GpioComponent.LOW:
            if (log.isDebugEnabled()) {
               log.debug("Setting pin " + endpoint.getPinName() + " to LOW state.");
            }
            output.low();
            if (pulseInMicroseconds > 0) {
               if (log.isDebugEnabled()) {
                  log.debug("Waiting for " + pulseInMicroseconds + " microseconds.");
               }
               BulldogUtil.sleepNs(pulseInMicroseconds * 1000L);
               if (log.isDebugEnabled()) {
                  log.debug("Setting pin " + endpoint.getPinName() + " to HIGH state.");
               }
               output.high();
            }
            break;
         default:
            // not supposed to happen
      }
   }
}
