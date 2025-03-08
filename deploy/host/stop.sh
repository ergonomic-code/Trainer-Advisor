#!/bin/bash

source ./secrets.sh || exit
docker --context qyoga-prod compose -p qyoga -f ../qyoga/docker-compose.yml stop
