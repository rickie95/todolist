FROM jenkins/jenkins:lts-alpine
EXPOSE 8080

USER root

RUN echo "http://dl-cdn.alpinelinux.org/alpine/edge/community" >> /etc/apk/repositories
RUN apk --update --no-cache add sudo curl maven=3.6.3-r0 xvfb docker
RUN echo "jenkins ALL=NOPASSWD: ALL" >> /etc/sudoers
ENV M2_HOME /usr/bin
COPY docker-files/docker-jenkins.sh /usr/local/bin/docker-jenkins.sh
RUN chmod +x /usr/local/bin/docker-jenkins.sh

USER jenkins

# Install plugins and copy configuration files
COPY jenkins-files/plugins.txt /var/jenkins_home/
RUN /usr/local/bin/install-plugins.sh < /var/jenkins_home/plugins.txt
COPY jenkins-files/config/*.xml /usr/share/jenkins/ref/
RUN echo 2.0 > /usr/share/jenkins/ref/jenkins.install.UpgradeWizard.state

# Add job
COPY jenkins-files/todolist-pipeline.xml /usr/share/jenkins/ref/jobs/todolist-pipeline/config.xml

USER root
ENTRYPOINT ["/sbin/tini", "--", "/usr/local/bin/docker-jenkins.sh"]


