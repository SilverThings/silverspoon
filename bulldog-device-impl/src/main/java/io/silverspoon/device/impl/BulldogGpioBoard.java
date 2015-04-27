package io.silverspoon.device.impl;

import io.silverspoon.bulldog.core.gpio.DigitalInput;
import io.silverspoon.bulldog.core.gpio.DigitalOutput;
import io.silverspoon.bulldog.core.platform.Board;
import io.silverspoon.bulldog.core.platform.Platform;
import io.silverspoon.device.api.board.GpioBoard;
import io.silverspoon.device.api.button.Button;
import io.silverspoon.device.api.gpio.DigitalInputPin;
import io.silverspoon.device.api.gpio.DigitalOutputPin;

public class BulldogGpioBoard implements GpioBoard {

   private Board board;

   public BulldogGpioBoard() {
      // Detect the board we are running on
      board = Platform.createBoard();
   }

   @Override
   public DigitalOutputPin getDigitalOutputPin(String pinName) {
      return new BulldogDigitalOutputPin(board.getPin(pinName).as(DigitalOutput.class));
   }

   @Override
   public DigitalInputPin getDigitalInputPin(String pinName) {
      return new BulldogDigitalInputPin(board.getPin(pinName).as(DigitalInput.class));
   }

   @Override
   public Button getButton(DigitalInputPin pin) {

      return null;
   }

   @Override
   public String getName() {
      return board.getName();
   }

}
