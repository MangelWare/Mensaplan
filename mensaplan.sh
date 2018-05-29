#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
java -Dfile.encoding=UTF-8 -classpath $DIR/out/production/Mensaplan:$DIR/jsoup-1.11.2.jar Mensaplan $1
