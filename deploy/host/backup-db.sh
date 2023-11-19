#!/bin/bash
docker --context qyoga-prod exec -t qyoga-postgres-1 pg_dumpall -c -U postgres > "/data/azhidkov/backups/qyoga/qyoga_dump_$(date +%d-%m-%Y"_"%H_%M_%S).sql"
