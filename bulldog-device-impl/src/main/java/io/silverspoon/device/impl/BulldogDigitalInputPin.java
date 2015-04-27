package io.silverspoon.device.impl;

import io.silverspoon.bulldog.core.gpio.DigitalInput;
import io.silverspoon.device.api.gpio.DigitalInputPin;

public class BulldogDigitalInputPin implements DigitalInputPin {

   private DigitalInput inputPin;

   public BulldogDigitalInputPin(DigitalInput inputPin) {
      this.inputPin = inputPin;
   }

   public DigitalInput getDigitalInputPin() {
      return inputPin;
   }
   
   @Override
   public String getName() {
      return inputPin.getName();
   }

}
