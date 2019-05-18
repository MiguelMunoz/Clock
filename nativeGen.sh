date

set -x
$JAVA_HOME/bin/native-image \
-H:ClassInitialization=java.awt.Component:run_time \
--initialize-at-run-time=`com.neptunedreams.clock.Face,sun.java2d.opengl.OGLRenderQueue \
-H:+ReportExceptionStackTraces \
-classpath \
target/clock-1.0-SNAPSHOT.jar \
com.neptunedreams.clock.GraalClock

# -H:+ReportExceptionStackTraces \
# -H:ClassInitialization=sun.java2d.opengl.OGLRenderQueue:rerun \
# --initialize-at-run-time=sun.java2d.opengl.OGLRenderQueue \
# -H:ClassInitialization=sun.java2d.opengl.OGLRenderQueue:run_time \


# --no-fallback \
# --allow-incomplete-classpath \
# --initialize-at-run-time=com.neptunedreams.skeleton.Skeleton \
# --initialize-at-build-time=org.sqlite.JDBC \
# --initialize-at-build-time=org.apache.derby.jdbc.AutoloadedDriver \

# target/libs/jsr305-1.3.9.jar:\

# -H:ClassInitialization=com.neptunedreams.clock.GraalClock:run_time \
# -H:ClassInitialization=sun.java2d.opengl.OGLRenderQueue:run_time \
