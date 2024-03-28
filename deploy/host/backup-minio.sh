#!/bin/bash
echo "Creating minio backup"

rsync -avzP root@trainer-advisor.pro:/var/minio/qyoga/ /data/azhidkov/backups/qyoga/minio