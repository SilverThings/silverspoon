/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.silverspoon;

import org.apache.camel.CamelContext;
import org.apache.camel.CamelException;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.UriEndpointComponent;

import java.util.Map;

import io.silverspoon.bulldog.core.Signal;

/**
 * Represents the component that manages {@link BulldogEndpoint}.
 *
 * @author <a href="mailto:pavel.macik@gmail.com">Pavel Macík</a>
 * @author <a href="mailto:pipistik.bunciak@gmail.com">Štefan Bunčiak</a>
 */
public class BulldogComponent extends UriEndpointComponent {

   public BulldogComponent() {
      super(BulldogEndpoint.class);
   }

   public BulldogComponent(CamelContext context) {
      super(context, BulldogEndpoint.class);
   }

   protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
      final BulldogEndpoint endpoint = new BulldogEndpoint(uri, this);
      setProperties(endpoint, parameters);

      // don't allow overriding of bus
      endpoint.setBus(remaining);

      // TODO: currently only gpio and i2c supported. Will be fixed with issues #35, and #38
      if (!remaining.equalsIgnoreCase("gpio") && !remaining.equalsIgnoreCase("i2c")) {
         throw new CamelException("Other bus than gpio or i2c is not supported at the moment.");
      }

      // if value is not null try to construct a valid Signal
      //if (endpoint.getValue() != null) {
      //   Signal.fromString(endpoint.getValue());
      //}

      //if (endpoint.getPin() != null) {
         return endpoint;
      //} else {
      //   throw new CamelException("The value of the 'pin' must not be null.");
      //}
   }
}
