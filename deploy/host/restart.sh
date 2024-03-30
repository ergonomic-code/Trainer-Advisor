#!/bin/bash

docker --context qyoga-prod compose -p qyoga -f ../qyoga/docker-compose.yml restart app
