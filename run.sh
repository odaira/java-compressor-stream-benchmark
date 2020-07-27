#!/bin/sh
#
#  Copyright 2020 Rei Odaira
#
#  Licensed under the Apache License, Version 2.0 (the "License");
#  you may not use this file except in compliance with the License.
#  You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.

if [ -n "$JAVA_HOME" ]; then
    java_cmd=$JAVA_HOME/bin/java
else
    java_cmd=java
fi

classpath=target/java-compressor-stream-benchmark-1.0-SNAPSHOT-jar-with-dependencies.jar
for lib in lib/*.jar; do
    classpath=$classpath:$lib
done

$java_cmd -cp $classpath $JAVA_OPTS net.odaira.jcsb.App $*
