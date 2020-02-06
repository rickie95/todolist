FROM jenkins/jenkins:lts-alpine
EXPOSE 8080
USER root

RUN echo "http://dl-cdn.alpinelinux.org/alpine/edge/community" >> /etc/apk/repositories
RUN apk --update --no-cache add openjdk8-jre sudo curl maven=3.6.3-r0
RUN echo "jenkins ALL=NOPASSWD: ALL" >> /etc/sudoers

USER jenkins

# Install plugins
COPY jenkins-files/plugins.txt /var/jenkins_home/plugins.txt
RUN /usr/local/bin/install-plugins.sh < /var/jenkins_home/plugins.txt

# Add job
COPY jenkins-files/todolist-pipeline.xml /usr/share/jenkins/ref/jobs/todolist-pipeline/config.xml

# Jenkins configurations files
COPY jenkins-files/config/*.xml /usr/share/jenkins/ref/
ENV JAVA_HOME=/usr/lib/jvm/java-1.8-openjdk
RUN echo 2.0 > /usr/share/jenkins/ref/jenkins.install.UpgradeWizard.state
