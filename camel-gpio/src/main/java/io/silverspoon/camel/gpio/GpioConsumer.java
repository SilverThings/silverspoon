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

import io.silverspoon.device.api.board.NoSupportedBoardFoundException;
import io.silverspoon.device.api.button.Button;
import io.silverspoon.device.api.button.ButtonListener;
import io.silverspoon.device.api.gpio.DigitalInputPin;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.ScheduledPollConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The GPIO consumer.
 *
 * @author Pavel Macík <pavel.macik@gmail.com>
 */
public class GpioConsumer extends ScheduledPollConsumer {
   private final GpioEndpoint endpoint;
   
   private static Map<String,DigitalInputPin> inputPins = Collections.synchronizedMap(new HashMap<String, DigitalInputPin>());
   private final DigitalInputPin inputPin;
   private final Button button;
   private final GpioButtonListener cbListener;
   private Queue<String> eventQueue = new LinkedBlockingQueue<>();

   private static final transient Logger log = LoggerFactory.getLogger(GpioConsumer.class);

   public GpioConsumer(GpioEndpoint endpoint, Processor processor) throws NoSupportedBoardFoundException {
      super(endpoint, processor);
      this.endpoint = endpoint;

      inputPin = GpioConsumer.getDigitalInputPin(endpoint.getPinName());
      if (log.isInfoEnabled() && inputPin != null) {
         log.info("Pin attached: " + inputPin.getName());
      }

      button = GpioEndpoint.getBoard().getButton(inputPin);
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

   private static DigitalInputPin getDigitalInputPin(String pinName) {
      if (!inputPins.containsKey(pinName)) {
         try {
            inputPins.put(pinName, GpioEndpoint.getBoard().getDigitalInputPin(pinName));
         } catch (NoSupportedBoardFoundException e) {
            log.error("Cannot create digital input PIN as there is no suitable board. ", e);
         }
      }

      return inputPins.get(pinName);
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
