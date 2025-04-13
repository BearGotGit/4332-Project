#!/bin/bash
PWD=$(pwd)
PROJ="$(pwd)/src/main/java/CLI"
OUT="$(pwd)/out/production/main/"

make clean; make
clear
# -cp is for classpath. This tells java to use the compiled files in the out dir
cd "$PROJ" && java -cp "$OUT" CLI.Main.CLI
cd "$PWD"