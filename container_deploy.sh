#!/bin/bash

container_name="orlp-server";
mysql_container="orlp-mysql";

# if [ ! "$(docker ps -aq -f name=$mysql_container)" ]; then
#     echo "Container $mysql_container is not running. Starting $mysql_container ...";
#     docker run --name $mysql_container --net=orlp --ip=192.168.0.50 -h $mysql_container --expose=3306 -e MYSQL_ROOT_PASSWORD=123456 -d mysql:latest
#     else
#     echo "$mysql_container already running. Restarting $mysql_container ...";
#     docker container stop $mysql_container && docker container rm -f $mysql_container;
#     docker run --name $mysql_container --net=orlp --ip=192.168.0.50 -h $mysql_container --expose=3306 -e MYSQL_ROOT_PASSWORD=123456 -d mysql:latest
# fi

# sleep 3;

if [ ! "$(docker ps -aq -f name=$container_name)" ]; then
    echo "Container $container_name is not running. Starting $container_name ...";
    docker run --name $container_name -d --restart=always --net=orlp --add-host orlp-mysql:192.168.0.50 --expose=80 --expose=443 -e 'VIRTUAL_HOST=api.infolve.com' -e 'LETSENCRYPT_EMAIL=holovko.oleksandr@gmail.com' -e 'LETSENCRYPT_HOST=api.infolve.com' -v /home/wercker/orlp-server:/usr/src/myapp -w /usr/src/myapp java:8-jre java -jar Spaced.Repetition.jar;
    else
    echo "$container_name already running. Restarting $container_name ...";
    docker container stop $container_name && docker container rm -f $container_name;
    docker run --name $container_name -d --restart=always --net=orlp --add-host orlp-mysql:192.168.0.50 --expose=80 --expose=443 -e 'VIRTUAL_HOST=api.infolve.com' -e 'LETSENCRYPT_EMAIL=holovko.oleksandr@gmail.com' -e 'LETSENCRYPT_HOST=api.infolve.com' -v /home/wercker/orlp-server:/usr/src/myapp -w /usr/src/myapp java:8-jre java -jar Spaced.Repetition.jar;
fi