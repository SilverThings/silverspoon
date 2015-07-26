package io.silverspoon;

import io.silverspoon.bulldog.core.Signal;
import io.silverspoon.bulldog.core.gpio.DigitalOutput;
import io.silverspoon.bulldog.core.gpio.Pin;
import io.silverspoon.bulldog.core.util.BulldogUtil;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;

/**
 * The Bulldog producer.
 */
public class BulldogProducer extends DefaultProducer {

   private BulldogEndpoint endpoint;

   private final Pin pin;
   private DigitalOutput output;

   public BulldogProducer(BulldogEndpoint endpoint) {
      super(endpoint);
      this.endpoint = endpoint;

      pin = endpoint.getBoard().getPin(endpoint.getPin());
      log.info("Pin attached: " + pin.getName());
      output = pin.as(DigitalOutput.class);
   }

   public void process(Exchange exchange) throws Exception {
      // log.debug(exchange.getIn().getBody().toString());
      final String value = endpoint.getValue();
      // if specified in URL set accordingly, otherwise use exchange body
      if (value != null) {
         setPinValue(Signal.fromString(value));
      } else {
         setPinValue(Signal.fromString(exchange.getIn().getBody().toString()));
      }
   }

   private void setPinValue(final Signal value) {
      final long pulseInMicroseconds = this.endpoint.getPulseInMicroseconds();

      switch (value) {
         case High:
            log.debug("Setting pin " + endpoint.getPin() + " to HIGH state.");
            output.high();

            if (pulseInMicroseconds > 0) {
               log.debug("Waiting for " + pulseInMicroseconds + " microseconds.");
               BulldogUtil.sleepNs(pulseInMicroseconds * 1000L);
               log.debug("Setting pin " + endpoint.getPin() + " to LOW state.");
               output.low();
            }

            break;
         case Low:
            log.debug("Setting pin " + endpoint.getPin() + " to LOW state.");
            output.low();

            if (pulseInMicroseconds > 0) {
               log.debug("Waiting for " + pulseInMicroseconds + " microseconds.");
               BulldogUtil.sleepNs(pulseInMicroseconds * 1000L);
               log.debug("Setting pin " + endpoint.getPin() + " to HIGH state.");
               output.high();
            }

            break;
         default:
            // not supposed to happen
      }
   }
}
