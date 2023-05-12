#!/bin/bash

SCRIPT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )

if [ "$EUID" -ne 0 ]
  then echo "Needs to be run as root!"
  exit 1
fi

ln -sf "$SCRIPT_DIR/mensaplan.sh" /usr/bin/mensaplan
echo "Symlink created"