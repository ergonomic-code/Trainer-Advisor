source config.sh

docker context create qyoga-prod
docker context update --docker "host=ssh://root@$QYOGA_PROD_IP" qyoga-prod