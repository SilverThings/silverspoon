package io.silverspoon.device;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * 
 * @author sbunciak
 *
 */
public class OneWireTemperatureSensor {
   private File deviceFile = null;

   /**
    * 
    * @param deviceFile
    */
   public OneWireTemperatureSensor(File deviceFile) {
      this.deviceFile = deviceFile;
   }

   /**
    * 
    * @return
    * @throws IOException
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
