# Clock
Simple Swing Class to test Native Image Generation using GraalVM.

To attempt to build the native image, run the `nativeGen.sh` batch file. 

As far as I can tell, right now is just not possible with Swing or AWT applications. This is because they launch the event-dispatch-Thread as soon as the application opens a window, which necessarily happens in the main method. Even completely removing the launch of an AWT window will fail to delay the loading of the AWT component hierarchy, which apparently launches the event thread.

— Miguel Muñoz, 5/17/2019
