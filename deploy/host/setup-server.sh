source config.sh

echo $QYOGA_PROD_IP > ../server/hosts
ansible-playbook -vvv -i ./hosts -u root qyoga-server.yaml