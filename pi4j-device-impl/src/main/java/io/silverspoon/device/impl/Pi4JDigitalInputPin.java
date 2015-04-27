package io.silverspoon.device.impl;

import io.silverspoon.device.api.gpio.DigitalInputPin;

import com.pi4j.io.gpio.GpioPinDigitalInput;

public class Pi4JDigitalInputPin implements DigitalInputPin {

   private GpioPinDigitalInput pin;

   public Pi4JDigitalInputPin(GpioPinDigitalInput pin) {
      this.pin = pin;
   }

   public String getName() {
      return pin.getName();
   }
   
   public GpioPinDigitalInput getPin() {
      return this.pin;
   }
}
