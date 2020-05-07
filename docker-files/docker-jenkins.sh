#! /bin/bash -e
getent group docker-host && delgroup --quiet docker-host
DOCKER_GID=$(stat -c '%g' ${DOCKER_SOCKET})
echo "Creating Docker User Group: $DOCKER_GIDP"
addgroup -g $DOCKER_GID docker-host
if [ $? -eq 0 ]; then 
    addgroup jenkins docker-host
fi
su -s /bin/bash -c "/sbin/tini -- /usr/local/bin/jenkins.sh" jenkins
