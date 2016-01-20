package io.silverspoon;

import io.silverspoon.bulldog.core.Signal;
import io.silverspoon.bulldog.core.gpio.DigitalOutput;
import io.silverspoon.bulldog.core.pin.Pin;
import io.silverspoon.bulldog.core.util.BulldogUtil;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;

/**
 * The Bulldog producer.
 */
public abstract class BulldogProducer extends DefaultProducer {

   private BulldogEndpoint endpoint;

   public BulldogProducer(BulldogEndpoint endpoint) {
      super(endpoint);
      this.endpoint = endpoint;
   }

   public abstract void process(Exchange exchange) throws Exception;

   public BulldogEndpoint getEndpoint(){
      return this.endpoint;
   }
}
