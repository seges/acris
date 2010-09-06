#!/bin/bash

if [ ${#@} -ne 1 ] ; then
	echo "Provide version argument"
	exit 42
fi

list="acris-os-build acris-os-parent acris-benchmark acris-binding acris-callbacks acris-client-core acris-common acris-external acris-generator acris-json acris-recorder acris-security-appengine acris-security-core acris-security-gilead acris-security-hibernate acris-security-jpa acris-security-parent acris-security-spring acris-security-spring-hibernate acris-security-spring-jpa acris-server-components acris-widgets acris-showcase-binding acris-showcase-callbacks acris-showcase-client-core acris-showcase-deployer acris-showcase-generator acris-showcase-json acris-showcase-mvp acris-showcase-parent acris-showcase-recorder acris-showcase-security acris-showcase-svn-post-commit acris-showcase-ui-support acris-showcase-widgets"

for project in $list ; do
	echo "Creating $project/$1"
	mkdir -p $project/$1
	svn add $project/$1
done

