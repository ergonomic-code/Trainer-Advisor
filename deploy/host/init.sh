export HOST=80.249.145.23

ansible-playbook -vvv -i ./hosts -u root qyoga-server.yaml

docker context create qyoga-prod
docker context update --docker "host=ssh://root@$HOST" qyoga-prod

docker --context qyoga-prod compose -p qyoga -f ../qyoga/docker-compose.yml up -d

echo $HOST > ../server/hosts