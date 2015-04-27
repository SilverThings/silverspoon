package io.silverspoon.device.impl;

import io.silverspoon.bulldog.core.gpio.DigitalOutput;
import io.silverspoon.device.api.gpio.DigitalOutputPin;

public class BulldogDigitalOutputPin implements DigitalOutputPin {

   private DigitalOutput outputPin;

   public BulldogDigitalOutputPin(DigitalOutput outputPin) {
      this.outputPin = outputPin;
   }

   @Override
   public void high() {
      outputPin.high();
   }

   @Override
   public void low() {
      outputPin.low();
   }

   @Override
   public void toggle() {
      outputPin.toggle();
   }

   @Override
   public boolean isHigh() {
      return outputPin.isHigh();
   }

   @Override
   public boolean isLow() {
      return outputPin.isLow();
   }

   @Override
   public String getName() {
      return outputPin.getName();
   }
}
