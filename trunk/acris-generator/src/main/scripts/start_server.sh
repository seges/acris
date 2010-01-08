RMI_INTERFACE=../rmi/target/rmi-1.0.0-SNAPSHOT.jar
M2_REPO=~/.m2/repository

CLASSPATH=`cat cp.txt`
PWD=`pwd`
export CLASSPATH=$CLASSPATH:$PWD/src/main/java:$PWD/src/main/resources:$PWD/../client/target/classes:$PWD/../client/src/main/java:$PWD/../client/src/main/resources:$PWD/target/classes

#java -classpath $CLASSPATH -Dgen="www-gen -out www-test" -Djava.rmi.server.codebase="$PWD/$RMI_INTERFACE" sk.seges.web.generator.service.ContentGeneratorService
java -Xmx512m -classpath $CLASSPATH -Djava.rmi.default=true sk.seges.web.generator.service.ContentGeneratorService
