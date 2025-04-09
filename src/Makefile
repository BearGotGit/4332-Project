JAVAC_FLAGS=-d
JAVA_FLAGS=-cp

# This Makefile is to compile the project
# Run it with either make or make all to compile
#	 It defaults to the first rule if u just use make
# Clean with make clean

all: out/Main/*.class Models/*.class # These are the dependencies
# This is the command it runs if it has the dependencies

run: out/Main/*.class Models/*.class
	java ${JAVA_FLAGS} out Main.CLI

out/Main/*.class Models/*.class: Main/*.java Models/*.java
# This is the command it runs if it has the dependencies
	javac ${JAVAC_FLAGS} out Main/*.java Models/*.java

clean:
	rm -rf out