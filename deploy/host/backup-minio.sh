#!/bin/bash
echo "Creating minio backup"

rsync -avzP root@qyoga.pro:/var/minio/qyoga/ /data/azhidkov/backups/qyoga/minio