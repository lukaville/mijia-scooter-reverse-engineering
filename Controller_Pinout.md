# **Controller Pinout :**

* 1 -Power plug. 

* 2 - Three terminals - motor windings A, B, C. Current sensors on each pole. 

* 3 - 5 pins - hall sensors on the motor. 
    + 5v, A, B, C , GND. No Temperature sensor. 

The controller checks sensors at power up. 
To run without a wheel, it is necessary to close the at least one signal pin to the ground, but not all 3. 

* 4 - 4 pins - the interface to the controller in the steering wheel. 
    signals are :
    GND - common 
    P - power from the battery, a high impedance output. 
    Data - Serial interface 
    + 5V - rises after the charge and feeds into the steering wheel. 
    The signal P turns on the scooter. 

* 5 - 3 Pin - interface to the BMS 
    signals are :
    RX/TX - serial interface. 
    L - line on the LED stop signal.

* 6 - G, D, C, SWD are unsoldered. Microcontroller is STM32F103C8T6. Closed Fuse for reading.

**Details from [Electrotransport.ru forum](http://electrotransport.ru/ussr/index.php?PHPSESSID=mpl26nqjfisk1q04l0sk0mir06&topic=43910.306#topmsg)**
