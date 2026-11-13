cd ../server || exit
ansible-playbook -v -i ./hosts -u root ./qyoga-server.yaml --tags "cert"
ssh root@ta.azhidkov.pro "sudo systemctl restart nginx.service"