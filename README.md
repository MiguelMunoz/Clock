# Clock
## Purpose

Simple Swing Class to test Native Image Generation using GraalVM. The point of this is to find a way to build a Swing or AWT application using native-image generation that does not need a JDK to run, and hence will load much faster. (Loading times are an achilles heel of Swing applications.) However, with the options available, I could not find a way to do this.

# Launch
If you want to launch the application, you first need to uncomment out the single line in GraalClock.main(), which reads `Face.doLaunch();`

The application doesn't do much. It exists solely to test the process of building a native-image Swing application. It uses no Threads beyond those required by Swing.

## Build
To attempt to build the native image, run the `nativeGen.sh` batch file.

### Experiments
I tried to delay the loading of two classes until runtime: `java.awt.Component` and `sun.java2d.opengl.OGLRenderQueue`. This didn't work. I ran my experiments 2 ways. First, I ran them with the the `Face.doLaunch();` call commented out. This gives me an application that does not open a window, so does nothing useful. Then I ran them with the `Face.doLaunch();` in place. Here are the results in each case:

#### `// Face.doLaunch();`

##### 1. OGLRenderQueue

When I used this option: `-H:ClassInitialization=sun.java2d.opengl.OGLRenderQueue:run_time` it gave me this error:

`Error: Class is already initialized, so it is too late to register delaying class initialization: sun.java2d.opengl.OGLRenderQueue for reason: from the command line`

##### 2. java.awt.Component
With this option: `-H:ClassInitialization=java.awt.Component:run_time` 
it gave me this error:

`Error: Class is already initialized, so it is too late to register delaying class initialization: java.awt.Component for reason: from the command line`

##### 3. `--initialize-at-run-time=java.awt.Component:run_time`
With this option: `--initialize-at-run-time=java.awt.Component:run_time` it built the application, which does nothing.

#### `Face.doLaunch();`

##### 1. OGLRenderQueue
When I used this option: `-H:ClassInitialization=sun.java2d.opengl.OGLRenderQueue:run_time` it gave me this error:

`Error: Class is already initialized, so it is too late to register delaying class initialization: java.awt.Component for reason: from the command line`


##### 2. java.awt.Component
With this option: `-H:ClassInitialization=java.awt.Component:run_time` 
it gave me the same error as above:

`Error: Class is already initialized, so it is too late to register delaying class initialization: java.awt.Component for reason: from the command line`

##### 3. `--initialize-at-run-time=java.awt.Component:run_time`
With this option: `--initialize-at-run-time=java.awt.Component:run_time` it built the application, but created a fallback image, which requires a JDK to run. It runs fine, but the load time is not improved, which defeats the purpose.

    com.oracle.svm.hosted.FallbackFeature$FallbackImageRequest: Aborting stand-alone image build.
    Detected a started Thread in the image heap. Threads running in the image generator are
    no longer running at image run time. The object was probably created by a class
    initializer and is reachable from a static field. By default, all class initialization is
    done during native image building.You can manually delay class initialization to image
    run time by using the option -H:ClassInitialization=<class-name>. Or you can write your
    own initialization methods and call them explicitly from your main entry point.
    Detailed message:
    Trace: 	object sun.java2d.opengl.OGLRenderQueue
      field sun.java2d.opengl.OGLRenderQueue.theInstance
    
      at com.oracle.svm.hosted.FallbackFeature.reportFallback(FallbackFeature.java:210)
      at com.oracle.svm.hosted.FallbackFeature.reportAsFallback(FallbackFeature.java:220)
      at com.oracle.svm.hosted.NativeImageGenerator.runPointsToAnalysis(NativeImageGenerator.java:733)
      at com.oracle.svm.hosted.NativeImageGenerator.doRun(NativeImageGenerator.java:523)
      at com.oracle.svm.hosted.NativeImageGenerator.lambda$run$0(NativeImageGenerator.java:441)
      at java.util.concurrent.ForkJoinTask$AdaptedRunnableAction.exec(ForkJoinTask.java:1386)
      at java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:289)
      at java.util.concurrent.ForkJoinPool$WorkQueue.runTask(ForkJoinPool.java:1056)
      at java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1692)
      at java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:157)
    Caused by: com.oracle.graal.pointsto.constraints.UnsupportedFeatureException: Detected a started
    Thread in the image heap. Threads running in the image generator are no longer running at
    image run time. The object was probably created by a class initializer and is reachable
    from a static field. By default, all class initialization is done during native image
    building.You can manually delay class initialization to image run time by using the
    option -H:ClassInitialization=<class-name>. Or you can write your own initialization 
    methods and call them explicitly from your main entry point.

Specifying `--no-fallback \` gave me this error message:

    Aborting stand-alone image build. Detected a started Thread in the image heap. Threads running in the image generator are no longer running at image run time. The object was probably created by a class initializer and is reachable from a static field. By default, all class initialization is done during native image building.You can manually delay class initialization to image run time by using the option -H:ClassInitialization=<class-name>. Or you can write your own initialization methods and call them explicitly from your main entry point.
    Detailed message:
    Trace: 	object sun.java2d.opengl.OGLRenderQueue
      field sun.java2d.opengl.OGLRenderQueue.theInstance

##### 4. Both OGLRenderQueue and Component

When I tried this option:

`--initialize-at-run-time=com.neptunedreams.clock.Face,sun.java2d.opengl.OGLRenderQueue,java.awt.Component`

I got this error:


`Error: Class is already initialized, so it is too late to register delaying class initialization: sun.java2d.opengl.OGLRenderQueue for reason: from the command line`

## Conclusion
As far as I can tell, right now is just not possible with Swing or AWT applications. This is because any Thread launched in the main method will run at build time rather than run time. But Swing applications launch the event-dispatch-Thread as soon as the application opens a window, which necessarily happens in the main method. Even completely removing the launch of an AWT window will fail to delay the loading of the AWT component hierarchy, which apparently launches the event thread.

If there was a way to specify an additional entry point to execute at runtime, this may be possible. I did not find such an option.

## Appendix
### Build Details

As of now, this will build a native image application that does not open a window, which means it does nothing. If I un-comment the line in GraalClock that says `Face.doLaunch`, then launch the application from a standard JDK, it will open the main window and give me a working application. If I try to build this, it will give me a fallback image, which requires a JDK to run, which defeats the purpose of this experiment. If I specify the --no-fallback option when building, it gives me an error.  

— Miguel Muñoz, 5/17/2019
