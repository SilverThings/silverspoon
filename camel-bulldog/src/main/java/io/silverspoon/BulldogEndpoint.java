package io.silverspoon;

import io.silverspoon.bulldog.core.platform.Board;
import io.silverspoon.bulldog.core.platform.Platform;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a Bulldog endpoint.
 */
public class BulldogEndpoint extends DefaultEndpoint {
   // PATTERN to match : bulldog://<bus>?pin=<pin_name>&value=<pin_value>
   // public static final String URI_PATTERN_STRING = "bulldog://(gpio|spi|i2c|pwm)(\\?[\\w=&%_]+)";
   // TODO: currently only gpio supported
   public static final String URI_PATTERN_STRING = "bulldog://gpio(\\?[\\w=&%_]+)";
   public static final Pattern URI_PATTERN = Pattern.compile(URI_PATTERN_STRING);

   private static final Logger LOG = LoggerFactory.getLogger(BulldogEndpoint.class);

   private final Board board;

   private String pin = null;

   private String value = null;

   private long pulseInMicroseconds = 0L;

   public BulldogEndpoint(String uri, BulldogComponent component) {
      super(uri, component);

      final Matcher m = URI_PATTERN.matcher(uri);
      if (!m.matches()) {
         throw new RuntimeException("Specified URI (" + uri + ") does not match the requested pattern (" + URI_PATTERN_STRING + ")");
      }

      board = Platform.createBoard();
      LOG.info("Board found: " + board.getName());
   }

   public Producer createProducer() throws Exception {
      return new BulldogProducer(this);
   }

   public Consumer createConsumer(Processor processor) throws Exception {
      if (value != null) {
         LOG.warn("Found value for pin. Omitting as creating consumer component.");
      }
      return new BulldogConsumer(this, processor);
   }

   public boolean isSingleton() {
      return true;
   }

   protected String getPin() {
      return this.pin;
   }

   public void setPin(String pin) {
      this.pin = pin;
   }

   protected Board getBoard() {
      return this.board;
   }

   protected String getValue() {
      return this.value;
   }

   public void setValue(String value) {
      this.value = value;
   }

   public long getPulseInMicroseconds() {
      return pulseInMicroseconds;
   }

   public void setPulseInMicroseconds(final long pulseInMicroseconds) {
      this.pulseInMicroseconds = pulseInMicroseconds;
   }
}
