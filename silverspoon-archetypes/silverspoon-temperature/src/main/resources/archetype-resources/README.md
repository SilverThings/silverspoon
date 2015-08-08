#Camel Temperature Archetype

Based on [Camel Servlet REST and Apache Tomcat example](https://github.com/apache/camel/tree/master/examples/camel-example-servlet-rest-tomcat).

##Prerequisites
Before using this archetype be sure to have w1 kernel modules loaded. Consult [camel-temperature](https://github.com/px3/silverspoon/tree/devel/camel-temperature) documentation for more details

##Running
You can try the example from Maven using

   mvn jetty:run

Once jetty is running you can reach the main page at http://localhost:8080/${artifactId}
