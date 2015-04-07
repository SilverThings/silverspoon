package io.silverspoon.device.impl;

import io.silverspoon.device.api.board.GpioBoard;
import io.silverspoon.device.api.button.Button;
import io.silverspoon.device.api.gpio.DigitalInputPin;
import io.silverspoon.device.api.gpio.DigitalOutputPin;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.system.SystemInfo;

import java.io.IOException;

public class Pi4JGpioBoard implements GpioBoard {

   // create gpio controller instance
   final GpioController gpio = GpioFactory.getInstance();

   public DigitalOutputPin getDigitalOutputPin(String pinName) {
      GpioPinDigitalOutput pin = gpio.provisionDigitalOutputPin(RaspiPin.getPinByName(pinName));
      return new Pi4JDigitalOutputPin(pin);
   }

   public DigitalInputPin getDigitalInputPin(String pinName) {
      GpioPinDigitalInput pin = gpio.provisionDigitalInputPin(RaspiPin.getPinByName(pinName));
      return new Pi4JDigitalInputPin(pin);
   }

   public String getName() {
      try {
         return SystemInfo.getBoardType().name();
      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } catch (InterruptedException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      
      return "Not detected!!!";
   }

   public Button getButton(DigitalInputPin pin) { 
      return new Pi4JButton(pin);
   }

}
