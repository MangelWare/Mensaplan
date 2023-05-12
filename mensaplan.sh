#!/bin/bash

JAR_FILE="$HOME/.m2/repository/de/simonmangel/mensaplan/1.0-SNAPSHOT/mensaplan-1.0-SNAPSHOT-jar-with-dependencies.jar"
java -jar "$JAR_FILE" $@
