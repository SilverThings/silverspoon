package io.silverspoon;

import org.apache.camel.CamelException;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
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
      final Message message = exchange.getIn();
      final String address = message.getHeader("address").toString();
      synchronized (lock) {
         if (log.isDebugEnabled()) {
            log.debug("Initializing I2C connection to address: " + address);
         }

         final String body = message.getBody().toString();
         final Matcher bodyMatcher = BODY_PATTERN.matcher(body);

         if (bodyMatcher.matches()) {
            exchange.getIn().setBody(send(address, body));
         } else {
            throw new CamelException("Message body [" + body + "] is invalid. It should be a sequence of hexadecimal character pairs.");
         }
      }
   }

   private String send(final String address, final String msg) throws Exception {
      final int length = getEndpoint().getReadLength();
      final byte[] buffer = new byte[length];
      final I2cConnection connection = i2c.createI2cConnection(Byte.decode(address));
      try {
         byte[] requestBuffer = new byte[msg.length() / 2];
         if (log.isTraceEnabled()) {
            log.trace("Preparing I2C message");
         }

         for (int i = 0; i < requestBuffer.length; i++) {
            final String value = "0x" + msg.substring(2 * i, 2 * (i + 1));
            requestBuffer[i] = Integer.decode(value).byteValue();
            if (log.isTraceEnabled()) {
               log.trace("Appending byte: " + value);
            }
         }
         if (log.isTraceEnabled()) {
            log.trace("Sending I2C message...");
         }
         connection.writeBytes(requestBuffer);
         if (log.isTraceEnabled()) {
            log.trace("I2C message sent");
         }
      } catch (IOException ioe) {
         ioe.printStackTrace();
         throw new IOException("Unable to write values to I2C bus (" + i2c.getName() + ") at address " + address + "!");
      }
      try {
         if (length > 0) {
            if (log.isTraceEnabled()) {
               log.trace("Recieving I2C response: ");
            }
            connection.readBytes(buffer);
            final StringBuffer response = new StringBuffer();
            for (int i = 0; i < length; i++) {
               response.append(Integer.toHexString(buffer[i]));
            }
            if (log.isTraceEnabled()) {
               log.trace(response);
            }
            return response.toString();
         } else {
            return "OK";
         }
      } catch (IOException ioe) {
         ioe.printStackTrace();
         throw new IOException("Unable to read values from I2C bus (" + i2c.getName() + ") at address " + address + "!");
      }
   }
}
