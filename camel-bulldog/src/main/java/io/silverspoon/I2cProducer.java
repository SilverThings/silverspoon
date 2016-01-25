package io.silverspoon;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;

import java.io.IOException;
import java.util.Arrays;

import io.silverspoon.bulldog.core.io.bus.i2c.I2cBus;
import io.silverspoon.bulldog.core.io.bus.i2c.I2cConnection;

/**
 * The Bulldog producer.
 */
public class I2cProducer extends BulldogProducer {

   private static final Object lock = new Object();
   private final I2cBus i2c;
   private I2cConnection connection;

   public I2cProducer(BulldogEndpoint endpoint) {
      super(endpoint);
      i2c = getEndpoint().getBoard().getI2cBuses().get(0);
   }

   public void process(Exchange exchange) throws Exception {
      final int length = getEndpoint().getReadLength();
      final byte[] buffer = new byte[length];
      boolean invalidData = false;
      synchronized (lock) {
         final I2cConnection connection = i2c.createI2cConnection(Byte.decode(getEndpoint().getAddress()));
         try {
            connection.writeBytes(exchange.getIn().getBody().toString().getBytes());
            if (length > 0) {
               connection.readBytes(buffer);
               StringBuffer out = new StringBuffer();
               for(int i = 0; i < length; i++){
                  out.append(Integer.toHexString(buffer[i]));
               }
               exchange.getIn().setBody(out.toString());
            }
         } catch (IOException ioe) {
            ioe.printStackTrace();
            throw new IOException("Unable to read values from I2C bus (" + i2c.getName() + ")!");
         }
      }
   }
}
