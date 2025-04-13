JAVAC_FLAGS=-d
JAVA_FLAGS=-cp

# This Makefile is to compile the project
# Use `make run` to compile and run
# Compile with either `make` or `make all` to compile
#	 It defaults to the first rule if u just use make
# Clean with make clean

all: out/Main/*.class # These are the dependencies

run: Main/*.java
	make clean && make execute

execute: out/Main/*.class
	clear; java ${JAVA_FLAGS} out CLI.Main.CLI

out/Main/*.class: Main/*.java Models/*.java
# This is the command it runs if it has the dependencies
	javac ${JAVAC_FLAGS} out Main/*.java Models/*.java

clean:
	rm -rf out