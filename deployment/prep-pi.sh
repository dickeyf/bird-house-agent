#!/bin/sh
# Installs openjdk 9 JRE onto a raspberry PI running raspbian
usage="./prep-pi.sh <pi host> <ssh-key> [username]"
# username defaults to 'pi'
# when ssh-key is not provided, ssh will prompt for password
#

if [[ -z "$1" ]]; then
  echo "You must provide the Raspberry PI host : ${usage}"
  exit 1
fi

pihost=$1

if [[ -z "$2" ]]; then
  echo "You must provide the ssh key files path : ${usage}"
  exit 1
else
  sshkey="-i $2"
fi

if [[ -z "$3" ]]; then
  username="pi"
else
  username=$3
fi

ssh ${sshkey} ${username}@${pihost} "sudo mkdir -p /etc/ssl/certs/java; sudo apt-get update; sudo apt-get install -y openjdk-8-jre"
