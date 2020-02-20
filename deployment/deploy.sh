#!/bin/sh
# Deploys the birdhouse agent onto a raspberry pi and installs it as a systemd service.
usage="./deploy.sh <pi host> <ssh-key> [username]"
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

#ssh ${sshkey} ${username}@${pihost} "mkdir -p /home/${username}/birdhouseagent"
#scp ${sshkey} ../target/*jar start.sh bird-house-agent.service ${username}@${pihost}:/home/${username}/birdhouseagent
ssh ${sshkey} ${username}@${pihost} "chmod +x birdhouseagent/start.sh;sudo cp -f birdhouseagent/bird-house-agent.service /etc/systemd/system;sudo systemctl enable bird-house-agent.service;sudo systemctl start bird-house-agent.service"
