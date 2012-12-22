#!/bin/bash

workdir=`dirname $0`
nohup  ./fork-server.sh professional jms/synapso >& $workdir/log-$1 &

