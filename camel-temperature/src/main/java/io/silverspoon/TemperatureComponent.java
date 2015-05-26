package io.silverspoon;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;

/**
 * Represents the component that manages {@link TemperatureEndpoint}.
 */
public class TemperatureComponent extends DefaultComponent {

   // TODO: check if /boot/config.txt file has been properly changed
   // TODO: check URL pattern
   // TODO: check kernel modules
   // TODO: add a placeholder for SPI/I2C-based implementation
   
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        Endpoint endpoint = new TemperatureEndpoint(uri, this);
        setProperties(endpoint, parameters);
        return endpoint;
    }
}
