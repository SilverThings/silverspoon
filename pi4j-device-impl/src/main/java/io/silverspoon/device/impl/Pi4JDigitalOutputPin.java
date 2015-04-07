package io.silverspoon.device.impl;

import io.silverspoon.device.api.gpio.DigitalOutputPin;

import com.pi4j.io.gpio.GpioPinDigitalOutput;

public class Pi4JDigitalOutputPin implements DigitalOutputPin {

   private GpioPinDigitalOutput pin;

   public Pi4JDigitalOutputPin(GpioPinDigitalOutput pin) {
      this.pin = pin;
   }

   public void high() {
      pin.high();
   }

   public void low() {
      pin.low();
   }

   public void toggle() {
      pin.toggle();
   }

   public boolean isHigh() {
      return pin.isHigh();
   }

   public boolean isLow() {
      return pin.isLow();
   }

   public String getName() {
      return pin.getName();
   }
}
