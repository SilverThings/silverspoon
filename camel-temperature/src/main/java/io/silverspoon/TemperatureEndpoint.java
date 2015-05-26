package io.silverspoon;

import io.silverspoon.device.OneWireTemperatureSensor;

import com.sun.istack.logging.Logger;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Temperature endpoint.
 */
public class TemperatureEndpoint extends DefaultEndpoint {

   private final String W1_DIR = "/sys/bus/w1/devices/";
   private List<OneWireTemperatureSensor> sensors = new ArrayList<OneWireTemperatureSensor>();
   private static final Logger LOG = Logger.getLogger(TemperatureEndpoint.class);

   public TemperatureEndpoint(String uri, TemperatureComponent component) {
      super(uri, component);
      loadSensors();
   }

   public Producer createProducer() throws Exception {
      return new TemperatureProducer(this);
   }

   public Consumer createConsumer(Processor processor) throws Exception {
      return new TemperatureConsumer(this, processor);
   }

   public boolean isSingleton() {
      return true;
   }

   private void loadSensors() {
      // Load sensors
      File sensorDir = new File(W1_DIR);
      for (File sensorFile : sensorDir.listFiles()) {
         if (!sensorFile.getName().startsWith("w1_bus_master")) {
            LOG.info("Adding sensor file: " + sensorFile);
            sensors.add(new OneWireTemperatureSensor(sensorFile));
         }
      }
   }

   // TODO: support more sensors at a time
   public float getTemperature() {
      float res = 0.00f;

      try {
         res = sensors.get(0).readTemperature();
      } catch (IOException e) {
         e.printStackTrace();
      }

      return res;
   }
}
