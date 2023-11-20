#!/bin/bash
cat </data/azhidkov/backups/qyoga/> | docker --context qyoga-prod exec -i qyoga-postgres-1 psql -U postgres
