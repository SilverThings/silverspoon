# Silverspoon LED Archetype

This demo project demonstrates capability of controlling GPIO pins directly from a Apache Camel route. In order to expose this functionality via REST API we are using Camel REST DSL.

## Running

Connect a LED diode to your computer and put the GPIO Pin name to

    src/main/resources/gpio.properties

Now you can start controlling your LED via REST interface by executing:

    mvn camel:run