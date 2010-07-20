#!/bin/bash

if [ ${#@} -ne 1 ] ; then
	echo "Provide version argument"
	exit 42
fi

list="sesam-core sesam-os sesam-os-build"

for project in $list ; do
	echo "Creating $project/$1"
	mkdir -p $project/$1
	svn add $project/$1
done

