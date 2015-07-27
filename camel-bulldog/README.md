Bulldog Camel Component Project
===============================

This project is a Camel component for [Bulldog](http://github.com/px3/bulldog) library.

To build this project on your local computer use:

    mvn install

You need to skip tests as they are designed to be run only on ARM boards like Raspberry Pi.
If you wish to enforce running tests use:

    mvn install -Ptests