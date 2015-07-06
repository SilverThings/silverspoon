package io.silverspoon;

import io.silverspoon.bulldog.core.Signal;
import io.silverspoon.bulldog.core.gpio.DigitalInput;
import io.silverspoon.bulldog.core.gpio.Pin;
import io.silverspoon.bulldog.devices.switches.Button;
import io.silverspoon.bulldog.devices.switches.ButtonListener;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.ScheduledPollConsumer;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The Bulldog consumer.
 */
public class BulldogConsumer extends ScheduledPollConsumer {
    private final BulldogEndpoint endpoint;

    private final Pin pin;
    private DigitalInput input;
    private Button button;
    private final GpioButtonListener cbListener;
    private Queue<String> eventQueue = new LinkedBlockingQueue<>();
    
    public BulldogConsumer(BulldogEndpoint endpoint, Processor processor) {
        super(endpoint, processor);
        this.endpoint = endpoint;
        
        pin = endpoint.getBoard().getPin(endpoint.getPinName());
        log.info("Pin attached: " + pin.getName());
        input = pin.as(DigitalInput.class);
        
        button = new Button(input, Signal.Low);
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
    
    private class GpioButtonListener implements ButtonListener {
       public void buttonPressed() {
          if (log.isInfoEnabled()) {
             log.info("Button at " + endpoint.getPinName() + " pressed!");
          }
          final String value = endpoint.getValue();
          // if value parameter is not set or is set to HIGH
          if (value == null || Signal.fromString(value).equals(Signal.High)) {
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
          if (value == null || Signal.fromString(value).equals(Signal.Low)) {
             final String msg = endpoint.getPinName() + ":0";
             if (log.isInfoEnabled()) {
                log.info("Adding a message to event queue: [" + msg + "]");
             }
             eventQueue.add(msg);
          }
       }
    }
}
