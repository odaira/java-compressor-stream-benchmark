#!/bin/sh

if [ -n "$JAVA_HOME" ]; then
    java_cmd=$JAVA_HOME/bin/java
else
    java_cmd=java
fi

classpath=target/java-compressor-stream-benchmark-1.0-SNAPSHOT-jar-with-dependencies.jar
for lib in lib/*.jar; do
    classpath=$classpath:$lib
done

$java_cmd -cp $classpath net.odaira.jcsb.App $*
