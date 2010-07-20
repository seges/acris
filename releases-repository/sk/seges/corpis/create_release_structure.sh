#!/bin/bash

if [ ${#@} -ne 1 ] ; then
	echo "Provide version argument"
	exit 42
fi

list="corpis-dao-api corpis-dao-impl corpis-os corpis-os-build corpis-test"

for project in $list ; do
	echo "Creating $project/$1"
	mkdir -p $project/$1
	svn add $project/$1
done

