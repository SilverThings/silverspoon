package io.silverspoon;

import org.apache.camel.CamelException;
import org.apache.camel.Exchange;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.silverspoon.bulldog.core.io.bus.i2c.I2cBus;
import io.silverspoon.bulldog.core.io.bus.i2c.I2cConnection;

/**
 * The I2C producer.
 *
 * @author <a href="mailto:pavel.macik@gmail.com">Pavel Macík</a>
 */
public class I2cProducer extends BulldogProducer {
   private static final Logger log = Logger.getLogger(I2cProducer.class);

   private static final Object lock = new Object();
   private final I2cBus i2c;
   private static final Pattern BODY_PATTERN = Pattern.compile("([0-9a-fA-F][0-9a-fA-F])*");

   public I2cProducer(BulldogEndpoint endpoint) {
      super(endpoint);
      i2c = getEndpoint().getBoard().getI2cBuses().get(0);
   }

   public void process(Exchange exchange) throws Exception {
      final int length = getEndpoint().getReadLength();
      final byte[] buffer = new byte[length];
      synchronized (lock) {
         if(log.isDebugEnabled()){
            log.debug("Initializing I2C connection to address: " + getEndpoint().getAddress());
         }
         final I2cConnection connection = i2c.createI2cConnection(Byte.decode(getEndpoint().getAddress()));
         final String body = exchange.getIn().getBody().toString();
         final Matcher bodyMatcher = BODY_PATTERN.matcher(body);
         try {
            if (bodyMatcher.matches()) {
               byte[] requestBuffer = new byte[body.length() / 2];
               if(log.isDebugEnabled()){
                  log.debug("Preparing I2C message");
               }
               for (int i = 0; i < requestBuffer.length; i++) {
                  final String value = "0x" + body.substring(2 * i, 2 * (i + 1))
                  requestBuffer[i] = Integer.decode(value).byteValue();
                  if(log.isDebugEnabled()){
                     log.debug("Appending byte: " + value);
                  }
               }
               if(log.isDebugEnabled()){
                  log.debug("Sending I2C message...");
               }
               connection.writeBytes(requestBuffer);
               if(log.isDebugEnabled()){
                  log.debug("I2C message sent");
               }
               if (length > 0) {
                  if(log.isDebugEnabled()){
                     log.debug("Recieving I2C response: ");
                  }
                  connection.readBytes(buffer);
                  final StringBuffer response = new StringBuffer();
                  for (int i = 0; i < length; i++) {
                     response.append(Integer.toHexString(buffer[i]));
                  }
                  if(log.isDebugEnabled()){
                     log.debug(response);
                  }
                  exchange.getIn().setBody(response.toString());
               } else {
                  exchange.getIn().setBody("OK");
               }
            } else {
               throw new CamelException("Message body [" + body + "] is invalid. It should be a sequence of hexadecimal character pairs.");
            }
         } catch (IOException ioe) {
            ioe.printStackTrace();
            throw new IOException("Unable to read values from I2C bus (" + i2c.getName() + ")!");
         }
      }
   }
}
