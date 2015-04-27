package io.silverspoon.device.impl;

import io.silverspoon.device.api.button.Button;
import io.silverspoon.device.api.button.ButtonListener;
import io.silverspoon.device.api.gpio.DigitalInputPin;

import com.pi4j.component.button.ButtonEvent;
import com.pi4j.component.button.ButtonPressedListener;
import com.pi4j.component.button.ButtonReleasedListener;
import com.pi4j.component.button.impl.GpioButtonComponent;

public class Pi4JButton implements Button {
   private GpioButtonComponent button;
   
   public Pi4JButton(DigitalInputPin pin) {
      Pi4JDigitalInputPin inputPin = (Pi4JDigitalInputPin) pin;
      
      button = new GpioButtonComponent(inputPin.getPin());
   }

   public void clearListeners() {
      button.removeAllListeners();
   }

   public void addListener(final ButtonListener listener) {
      // delegate listener
      button.addListener(new ButtonPressedListener() {
         
         public void onButtonPressed(ButtonEvent arg0) {
            listener.buttonPressed();
         }
      });
      
      button.addListener(new ButtonReleasedListener() {
         
         public void onButtonReleased(ButtonEvent arg0) {
            listener.buttonReleased();
         }
      });
   }
}
