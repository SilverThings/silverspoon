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
import org.apache.camel.Processor;
import org.apache.camel.impl.ScheduledPollConsumer;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import io.silverspoon.bulldog.core.Signal;
import io.silverspoon.bulldog.core.gpio.DigitalInput;
import io.silverspoon.bulldog.core.gpio.Pin;
import io.silverspoon.bulldog.devices.switches.Button;
import io.silverspoon.bulldog.devices.switches.ButtonListener;

/**
 * The GPIO consumer.
 *
 * @author Pavel Macík <pavel.macik@gmail.com>
 */
public class GpioConsumer extends ScheduledPollConsumer {
   private final GpioEndpoint endpoint;
   private final Pin pin;
   private final Button button;
   private final GpioButtonListener cbListener;
   private Queue<String> eventQueue = new LinkedBlockingQueue<>();

   public GpioConsumer(GpioEndpoint endpoint, Processor processor) {
      super(endpoint, processor);
      this.endpoint = endpoint;
      pin = endpoint.getBoard().getPin(endpoint.getPinName());
      if (log.isInfoEnabled()) {
         log.info("Pin attached: " + pin.getName());
      }

      button = new Button(pin.as(DigitalInput.class), Signal.High);
      cbListener = new GpioButtonListener();
   }

   @Override
   protected int poll() throws Exception {
      int count = 0;
      String event;
      while (!eventQueue.isEmpty()) {
         event = eventQueue.remove();
         Exchange exchange = endpoint.createExchange();
         exchange.getIn().setBody(event);

         try {
            // send message to next processor in the route
            getProcessor().process(exchange);
            count++;
         } finally {
            // log exception if an exception occurred and was not handled
            if (exchange.getException() != null) {
               getExceptionHandler().handleException("Error processing exchange", exchange, exchange.getException());
            }
         }
      }
      return count;
   }

   @Override
   protected void doStart() throws Exception {
      super.doStart();
      if (log.isInfoEnabled()) {
         log.info("Starting " + cbListener.getClass().getSimpleName());
      }
      button.addListener(cbListener);
   }

   @Override
   protected void doStop() throws Exception {
      super.doStop();
      button.clearListeners();
      if (log.isInfoEnabled()) {
         log.info("Stopping " + cbListener.getClass().getSimpleName());
      }
   }

   private class GpioButtonListener implements ButtonListener {
      public void buttonPressed() {
         if (log.isInfoEnabled()) {
            log.info("Button at " + endpoint.getPinName() + " pressed!");
         }
         final String value = endpoint.getValue();
         // if value parameter is not set or is set to HIGH
         if (value == null || GpioComponent.HIGH.equals(value)) {
            final String msg = endpoint.getPinName() + ":1";
            if (log.isInfoEnabled()) {
               log.info("Adding a message to event queue: [" + msg + "]");
            }
            eventQueue.add(msg);
         }
      }

      public void buttonReleased() {
         if (log.isInfoEnabled()) {
            log.info("Button at " + endpoint.getPinName() + " released!");
         }
         final String value = endpoint.getValue();
         // if value parameter is not set or is set to LOW
         if (value == null || GpioComponent.LOW.equals(value)) {
            final String msg = endpoint.getPinName() + ":0";
            if (log.isInfoEnabled()) {
               log.info("Adding a message to event queue: [" + msg + "]");
            }
            eventQueue.add(msg);
         }
      }
   }
}
