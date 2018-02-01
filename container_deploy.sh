#!/bin/bash

container_name="orlp-server";
mysql_container="orlp-mysql";

if [ ! "$(docker ps -aq -f name=$mysql_container)" ]; then
    echo "Container $mysql_container is not running. Starting $mysql_container ...";
    docker run --name $mysql_container --net=orlp --ip=192.168.0.50 -h $mysql_container --expose=3306 -e MYSQL_ROOT_PASSWORD=123456 -d mysql:latest
    else
    echo "$mysql_container already running. Restarting $mysql_container ...";
    docker container stop $mysql_container && docker container rm -f $mysql_container;
    docker run --name $mysql_container --net=orlp --ip=192.168.0.50 -h $mysql_container --expose=3306 -e MYSQL_ROOT_PASSWORD=123456 -d mysql:latest
fi

sleep 3;

if [ ! "$(docker ps -aq -f name=$container_name)" ]; then
    echo "Container $container_name is not running. Starting $container_name ...";
    docker run --name $container_name -d --net=orlp --add-host orlp-mysql:192.168.0.50 -p 8080:8080 --expose=1443 -e 'VIRTUAL_HOST=infolve.com:8080' -e 'VIRTUAL_PROTO=https' -v /home/wercker/orlp-server:/usr/src/myapp -w /usr/src/myapp java:8-jre java -jar Spaced.Repetition.jar;
    else
    echo "$container_name already running. Restarting $container_name ...";
    docker container stop $container_name && docker container rm -f $container_name;
    docker run --name $container_name -d --net=orlp --add-host orlp-mysql:192.168.0.50 -p 8080:8080 --expose=1443 -e 'VIRTUAL_HOST=infolve.com:8080' -e 'VIRTUAL_PROTO=https' -v /home/wercker/orlp-server:/usr/src/myapp -w /usr/src/myapp java:8-jre java -jar Spaced.Repetition.jar;
fi