package io.silverspoon.device;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Class to represent a OneWire Temperature sensor and read its values.
 * 
 * @author sbunciak
 * @see {@link TemperatureSensor}
 */
public class OneWireTemperatureSensor implements TemperatureSensor {
   private File deviceFile = null;

   /**
    * Constructor.
    * @param deviceFile
    */
   public OneWireTemperatureSensor(File deviceFile) {
      this.deviceFile = deviceFile;
   }

   /**
    * Read temperature from the sensors.
    * @return temperature
    * @throws IOException
    * @see {@link TemperatureSensor#readTemperature()}
    */
   public float readTemperature() throws IOException {

      byte[] encoded = Files.readAllBytes(new File(deviceFile, "w1_slave").toPath());

      String tmp = new String(encoded);
      int tmpIndex = tmp.indexOf("t=");

      if (tmpIndex < 0) {
         throw new IOException("Could not read temperature!");
      }

      return Integer.parseInt(tmp.substring(tmpIndex + 2).trim()) / 1000f;
   }
}
