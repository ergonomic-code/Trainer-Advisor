#!/bin/bash
echo "Creating db backup"

docker --context qyoga-prod exec -t qyoga-postgres-1 pg_dumpall -c -U postgres > "/data/azhidkov/backups/qyoga/postgres/qyoga_dump_$(date +%y%m%d-%H%M%S).sql"
