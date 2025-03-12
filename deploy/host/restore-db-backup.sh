#!/bin/bash
source ./secrets.sh || exit
cat $BACKUPS_PATH/qyoga_dump_250303-154519.sql | docker --context qyoga-prod exec -i qyoga-postgres-1 psql -U postgres
