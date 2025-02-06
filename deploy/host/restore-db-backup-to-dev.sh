#!/bin/bash
source ./secrets.sh || exit
cat $BACKUPS_PATH/qyoga_dump_250107-172452.sql | docker  exec -i qyoga-infra-dev-qg-pg-dev psql -U postgres
