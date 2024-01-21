source config.sh

echo "Enter Trainer Advisor admin password"
read TA_ADMIN_PASS

echo "export TA_ADMIN_PASS=$TA_ADMIN_PASS" > secrets.sh

docker context create qyoga-prod
docker context update --docker "host=ssh://root@$QYOGA_PROD_IP" qyoga-prod