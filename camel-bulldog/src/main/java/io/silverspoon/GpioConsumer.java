/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.silverspoon;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import io.silverspoon.bulldog.core.Signal;
import io.silverspoon.bulldog.core.gpio.DigitalInput;
import io.silverspoon.bulldog.core.pin.Pin;
import io.silverspoon.bulldog.devices.switches.Button;
import io.silverspoon.bulldog.devices.switches.ButtonListener;

/**
 * The GPIO consumer.
 *
 * @author <a href="mailto:pavel.macik@gmail.com">Pavel Macík</a>
 */
public class GpioConsumer extends BulldogConsumer {
   private final Pin pin;
   private DigitalInput input;
   private Button button;
   private final GpioButtonListener cbListener;
   private Queue<String> eventQueue = new LinkedBlockingQueue<>();

   public GpioConsumer(BulldogEndpoint endpoint, Processor processor) {
      super(endpoint, processor);

      pin = endpoint.getBoard().getPin(endpoint.getPin());
      log.info("Pin attached: " + pin.getName());
      input = pin.as(DigitalInput.class);

      button = new Button(input, Signal.Low);
      cbListener = new GpioButtonListener();
   }

   @Override
   protected int poll() throws Exception {
      int count = 0;
      String event;
      while (!eventQueue.isEmpty()) {
         event = eventQueue.remove();
         Exchange exchange = getEndpoint().createExchange();
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
            log.info("Button at " + getEndpoint().getPin() + " pressed!");
         }
         final String value = getEndpoint().getValue();
         // if value parameter is not set or is set to HIGH
         if (value == null || Signal.fromString(value).equals(Signal.High)) {
            final String msg = getEndpoint().getPin() + ":1";
            if (log.isInfoEnabled()) {
               log.info("Adding a message to event queue: [" + msg + "]");
            }
            eventQueue.add(msg);
         }
      }

      public void buttonReleased() {
         if (log.isInfoEnabled()) {
            log.info("Button at " + getEndpoint().getPin() + " released!");
         }
         final String value = getEndpoint().getValue();
         // if value parameter is not set or is set to LOW
         if (value == null || Signal.fromString(value).equals(Signal.Low)) {
            final String msg = getEndpoint().getPin() + ":0";
            if (log.isInfoEnabled()) {
               log.info("Adding a message to event queue: [" + msg + "]");
            }
            eventQueue.add(msg);
         }
      }
   }
}
