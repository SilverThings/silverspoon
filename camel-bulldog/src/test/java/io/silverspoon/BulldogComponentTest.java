package io.silverspoon;

import io.silverspoon.bulldog.core.Signal;
import io.silverspoon.bulldog.core.gpio.DigitalOutput;
import io.silverspoon.bulldog.core.platform.Board;
import io.silverspoon.bulldog.core.platform.Platform;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class BulldogComponentTest extends CamelTestSupport {

   private final String pinName = System.getProperty("test.pinName", "P1_7");
   private final String pinValue = System.getProperty("test.pinValue", "HIGH");

   @Test
   public void testBulldog() throws Exception {
      MockEndpoint mock = getMockEndpoint("mock:result");
      mock.expectedMinimumMessageCount(1);

      assertMockEndpointsSatisfied();

      // assert pin value
      Board board = Platform.createBoard();
      DigitalOutput outputPin = board.getPin(pinName).as(DigitalOutput.class);

      Signal signal = Signal.fromString(pinValue);
      if (signal.equals(Signal.High)) {
         assertTrue(outputPin.isHigh());
      } else {
         assertTrue(outputPin.isLow());
      }
   }

   @Override
   protected RouteBuilder createRouteBuilder() throws Exception {
      return new RouteBuilder() {
         public void configure() {
            from("direct:start").to(String.format("bulldog://gpio?pin=%s&value=%s", pinName, pinValue)).to("mock:result");
         }
      };
   }
}
