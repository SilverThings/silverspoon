package io.silverspoon;

import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;

import java.util.Map;

/**
 * Represents the component that manages {@link TemperatureEndpoint}.
 */
public class TemperatureComponent extends DefaultComponent {

   protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        Endpoint endpoint = new TemperatureEndpoint(uri, this);
        setProperties(endpoint, parameters);
        return endpoint;
    }
}
