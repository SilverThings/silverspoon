package io.silverspoon;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Ignore;
import org.junit.Test;

// TODO: enable tests
@Ignore
public class TemperatureComponentTest extends CamelTestSupport {

    @Test
    public void testTemperature() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:result");
        mock.expectedMinimumMessageCount(1);       
        
        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            public void configure() {
                from("temperature://foo")
                  .to("temperature://bar")
                  .to("mock:result");
            }
        };
    }
}
