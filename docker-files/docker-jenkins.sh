#! /bin/bash -e
getent group docker-host && delgroup --quiet docker-host
DOCKER_USER_GROUP=$(stat -c '%g' ${DOCKER_SOCKET})
echo "Creating Docker User Group: $DOCKER_USER_GROUP"
addgroup -g $DOCKER_USER_GROUP docker-host
addgroup jenkins docker-host
su -s /bin/bash -c "/sbin/tini -- /usr/local/bin/jenkins.sh" jenkins
