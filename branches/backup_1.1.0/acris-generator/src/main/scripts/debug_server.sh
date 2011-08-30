#!/bin/bash

RMI_INTERFACE=../rmi/target/rmi-1.0.0-SNAPSHOT.jar
M2_REPO=~/.m2/repository

CLASSPATH=`cat cp.txt`
#CLASSPATH=$filecontent
#export CLASSPATH=%CLASSPATH%;src\main\java;src\main\resources;..\client\target\classes;..\client\src\main\java;..\client\src\main\resources;target\classes;
export CLASSPATH=$CLASSPATH:src/main/java:src/main/resources:../client/target/classes:../client/src/main/java:../client/src/main/resources:target/classes

# java -classpath $CLASSPATH -Dgen="www-gen -out www-test" -Djava.rmi.server.codebase="%~dp0/$RMI_INTERFACE" sk.seges.web.generator.service.ContentGeneratorService
java -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,server=y,address=8001,suspend=y -Xmx512m -classpath $CLASSPATH -Djava.rmi.default=true sk.seges.web.generator.service.ContentGeneratorService
