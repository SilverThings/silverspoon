Camel Component Project
=======================

This project is a template of a Camel component.

To build this project use

    mvn install

Prerequisites to using this component
------------------------------------- 
* w1 kernel modules loaded

To load the 1-wire communication device kernel modules required to use the temperature sensor execute the following:
    sudo modprobe w1-gpio && sudo modprobe w1_therm

(to avoid having to manually enter this command every time you re-boot your Pi, edit using nano or similar the file "/etc/modules" adding the lines w1-gpio, and w1_therm to the end of the file.)

Since the Raspbian operating system was updated back at the end of January 2015 (kernel 3.18.8 and higher) which enabled Device Tree, the above steps do not result in the temperature sensor(s) being detected.

Now you need to open the /boot/config.txt file for editing. Then scroll down to the bottom of the file, and add the line:
`dtoverlay=w1-gpio`

Finally reboot the Raspberry Pi so that the changes take effect.