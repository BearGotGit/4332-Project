SRC=src/main/java/CLI
OUT=out
BACK_OUT=../../../../${OUT}

JAVAC_FLAGS=-d
JAVA_FLAGS=-cp

# This Makefile is to compile the project
# Use `make run` to compile and run
# Compile with either `make` or `make all` to compile
#	 It defaults to the first rule if u just use make
# Clean with make clean

.PHONY: all run execute clean # Tells make that these are keywords. Not necessary but helpful

all: ${OUT}/Main/*.class # These are the dependencies

run: ${SRC}/Main/*.java
	make clean && make execute

execute: ${OUT}/Main/*.class
	clear; cd ${SRC} && java ${JAVA_FLAGS} ${BACK_OUT} CLI.Main.Main

${OUT}/Main/*.class: ${SRC}/Main/*.java ${SRC}/Models/*.java ${SRC}/Helpers/*.java
# This is the command it runs if it has the dependencies
	cd ${SRC} && javac ${JAVAC_FLAGS} ${BACK_OUT} Main/*.java Models/*.java Helpers/*.java

clean:
	rm -rf ${OUT}