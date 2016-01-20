package io.silverspoon;

import org.apache.camel.Exchange;

import io.silverspoon.bulldog.core.Signal;
import io.silverspoon.bulldog.core.gpio.DigitalOutput;
import io.silverspoon.bulldog.core.pin.Pin;
import io.silverspoon.bulldog.core.util.BulldogUtil;

/**
 * The Bulldog producer.
 */
public class GpioProducer extends BulldogProducer {

   private final Pin pin;
   private DigitalOutput output;

   public GpioProducer(BulldogEndpoint endpoint) {
      super(endpoint);

      pin = endpoint.getBoard().getPin(endpoint.getPin());
      log.info("Pin attached: " + pin.getName());
      output = pin.as(DigitalOutput.class);
   }

   public void process(Exchange exchange) throws Exception {
      // log.debug(exchange.getIn().getBody().toString());
      final String value = getEndpoint().getValue();
      // if specified in URL set accordingly, otherwise use exchange body
      if (value != null) {
         setPinValue(Signal.fromString(value));
      } else {
         setPinValue(Signal.fromString(exchange.getIn().getBody(String.class)));
      }
   }

   private void setPinValue(final Signal value) {
      final long pulseInMicroseconds = getEndpoint().getPulseInMicroseconds();

      switch (value) {
         case High:
            log.debug("Setting pin " + getEndpoint().getPin() + " to HIGH state.");
            output.high();

            if (pulseInMicroseconds > 0) {
               log.debug("Waiting for " + pulseInMicroseconds + " microseconds.");
               BulldogUtil.sleepNs(pulseInMicroseconds * 1000L);
               log.debug("Setting pin " + getEndpoint().getPin() + " to LOW state.");
               output.low();
            }

            break;
         case Low:
            log.debug("Setting pin " + getEndpoint().getPin() + " to LOW state.");
            output.low();

            if (pulseInMicroseconds > 0) {
               log.debug("Waiting for " + pulseInMicroseconds + " microseconds.");
               BulldogUtil.sleepNs(pulseInMicroseconds * 1000L);
               log.debug("Setting pin " + getEndpoint().getPin() + " to HIGH state.");
               output.high();
            }

            break;
         default:
            // not supposed to happen
      }
   }
}
