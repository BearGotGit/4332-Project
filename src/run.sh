#!/bin/bash
PROJ=$(pwd)
make clean; make
# -cp is for classpath. This tells java to use the compiled files in the out dir
java -cp out Main.CLI # or `make run``