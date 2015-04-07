package io.silverspoon.device.impl;

import io.silverspoon.bulldog.core.Signal;
import io.silverspoon.device.api.button.Button;
import io.silverspoon.device.api.button.ButtonListener;
import io.silverspoon.device.api.gpio.DigitalInputPin;

public class BulldogButton implements Button {

   private io.silverspoon.bulldog.devices.switches.Button button;

   public BulldogButton(DigitalInputPin inputPin) {
      BulldogDigitalInputPin bulldogPin = (BulldogDigitalInputPin) inputPin;
      button = new io.silverspoon.bulldog.devices.switches.Button(bulldogPin.getDigitalInputPin(), Signal.High);
   }

   @Override
   public void clearListeners() {
      button.clearListeners();
   }

   @Override
   public void addListener(final ButtonListener listener) {
      button.addListener(new io.silverspoon.bulldog.devices.switches.ButtonListener() {
         @Override
         public void buttonPressed() {
            listener.buttonPressed();
         }

         @Override
         public void buttonReleased() {
            listener.buttonReleased();
         }
      });
   }
}
