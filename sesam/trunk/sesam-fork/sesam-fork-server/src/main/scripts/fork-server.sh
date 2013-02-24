#!/bin/bash

WORKDIR=`dirname $0`

if [[ $# -lt 2 ]] ; then
	echo "Provide config type and JNDI identifier for Fork Server. Example identifier: jms/synapso"
	exit 42
fi

CONFIG_SUFFIX=$1
CONFIG="$WORKDIR/env-$CONFIG_SUFFIX.cfg"
shift

if [[ ! -f "$CONFIG" ]] ; then
	echo "Please provide valid config file expected at $CONFIG"
	exit 42
fi

. $CONFIG

echo "JAVA_HOME=$JAVA_HOME"
echo "arguments=$@"

$JAVA_HOME/bin/java -cp lib/*:lib/sesam-fork-server-${project.version}.jar -XX:+UseCompressedOops -XX:MaxPermSize=5M -Xms5m -Xmx5M -Dfile.encoding=UTF-8 -Djava.naming.factory.initial=com.sun.jndi.fscontext.RefFSContextFactory -Djava.naming.provider.url=file://$BINDINGS_DIR sk.seges.sesam.fork.server.ForkServer $@


