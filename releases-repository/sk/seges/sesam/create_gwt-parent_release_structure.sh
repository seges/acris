#!/bin/bash

if [ ${#@} -ne 1 ] ; then
	echo "Provide version argument"
	exit 42
fi

list="sesam-os-base-parent"

for project in $list ; do
	echo "Creating $project/$1"
	mkdir -p $project/$1
	svn add $project/$1
done

