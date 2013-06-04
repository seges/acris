#!/bin/bash

./start_rmi.sh
mvn -Dmaven.test.skip clean package
mvn dependency:build-classpath
./start_server.sh
rm -f cp.txt

echo "** Finished successfully **"
