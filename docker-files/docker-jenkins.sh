#! /bin/bash

getent group docker-host && delgroup --quiet docker-host
DOCKER_GID=$(stat -c '%g' ${DOCKER_SOCKET})
echo "Creating Docker User Group: $DOCKER_GIDP"
addgroup -g $DOCKER_GID docker-host
if [ "$?" -eq 1 ] ; then 
	echo "Group docker_host already exists. This is fine if you already executed this container"
fi
addgroup jenkins docker-host
if [ "$?" -eq 0 ] ; then
	echo "User jenkins added to docker-host group"
else
	echo "User jenkins already in docker-host group" 
fi

su -s /bin/bash -c "/usr/local/bin/jenkins.sh" jenkins
