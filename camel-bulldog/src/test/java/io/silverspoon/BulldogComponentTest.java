package io.silverspoon;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Ignore;
import org.junit.Test;

// TODO: Enable tests
@Ignore
public class BulldogComponentTest extends CamelTestSupport {

    @Test
    public void testBulldog() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:result");
        mock.expectedMinimumMessageCount(1);       
        
        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            public void configure() {
                from("bulldog://foo")
                  .to("bulldog://bar")
                  .to("mock:result");
            }
        };
    }
}
