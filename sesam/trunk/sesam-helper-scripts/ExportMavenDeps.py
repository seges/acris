#!/usr/bin/env python

import sys
from shutil import *
from glob import *
from os import *
from os.path import *
from logging import *
import logging

def main(args):
	basicConfig(level=logging.DEBUG, format='%(asctime)s %(levelname)s %(message)s')

	srcDir = args[0]
	version = args[1]
	target = args[2]

	allFiles = glob(srcDir + sep + "*")
	for aFile in allFiles:
		debug("processing " + aFile)
		if isdir(aFile):
			processDir(aFile + sep + version, target)

def processDir(srcDir, target):
	allFiles = glob(srcDir + sep + "*")
	debug("dir = " + srcDir + ", target = " + target)
	for aFile in allFiles:
		if isfile(aFile):
			debug("is file = " + aFile)
			if aFile.endswith(".jar") or aFile.endswith(".pom") or aFile.endswith(".asc") or aFile.endswith(".war"):
				copy2(aFile, target)


if __name__ == '__main__':
	main(sys.argv[1:])

