#!/bin/bash
./backup-db.sh || exit
./backup-minio.sh || exit
./deploy-only.sh || exit
