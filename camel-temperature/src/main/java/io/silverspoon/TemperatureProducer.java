package io.silverspoon;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;

/**
 * The Temperature producer.
 */
public class TemperatureProducer extends DefaultProducer {
   // private static final Logger LOG = LoggerFactory.getLogger(TemperatureProducer.class);
   private TemperatureEndpoint endpoint;

   public TemperatureProducer(TemperatureEndpoint endpoint) {
      super(endpoint);
      this.endpoint = endpoint;
   }

   public void process(Exchange exchange) throws Exception {
      // System.out.println(exchange.getIn().getBody());
      exchange.getIn().setBody(endpoint.getTemperature());
   }
}
