#!/bin/bash

JAR_FILE="$HOME/.m2/repository/de/simonmangel/mensaplan/1.0/mensaplan-1.0-jar-with-dependencies.jar"
java -jar "$JAR_FILE" $@
