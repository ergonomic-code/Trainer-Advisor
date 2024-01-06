#!/bin/bash
./backup-db.sh
./backup-minio.sh

docker --context qyoga-prod compose -p qyoga -f ../qyoga/docker-compose.yml pull
docker --context qyoga-prod compose -p qyoga -f ../qyoga/docker-compose.yml up -d
