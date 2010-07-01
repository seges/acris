#!/bin/bash

if [ ${#@} -ne 1 ] ; then
	echo "Provide version argument"
	exit 42
fi

list="acris-os-build acris-os-parent acris-benchmark acris-binding acris-callbacks acris-client-core acris-json acris-common acris-external acris-generator acris-recorder acris-security acris-server-components"

for project in $list ; do
	echo "Creating $project/$1"
	mkdir -p $project/$1
	svn add $project/$1
done

