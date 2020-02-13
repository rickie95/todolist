FROM jenkins/jenkins:lts-alpine
EXPOSE 8080
USER root

RUN echo "http://dl-cdn.alpinelinux.org/alpine/edge/community" >> /etc/apk/repositories
RUN apk --update --no-cache add openjdk8-jre sudo curl maven=3.6.3-r0
RUN echo "jenkins ALL=NOPASSWD: ALL" >> /etc/sudoers

USER jenkins

# Install plugins
COPY jenkins-files/*.txt /var/jenkins_home/
RUN /usr/local/bin/install-plugins.sh < /var/jenkins_home/plugins.txt

# Add job
COPY jenkins-files/todolist-pipeline.xml /usr/share/jenkins/ref/jobs/todolist-pipeline/config.xml

# Jenkins configurations files
COPY jenkins-files/config/*.xml /usr/share/jenkins/ref/
ENV JAVA_HOME=/usr/lib/jvm/java-1.8-openjdk
ENV MAVEN_VERSION=${MAVEN_VERSION}
ENV M2_HOME /usr/bin/maven
ENV maven.home $M2_HOME
ENV M2 $M2_HOME/bin
ENV PATH $M2:$PATH

RUN echo 2.0 > /usr/share/jenkins/ref/jenkins.install.UpgradeWizard.state
#COPY jenkins-files/env_setup.sh /home/env_setup.sh
#COPY jenkins-files/*-token.txt /home/
#CMD ["/bin/bash", "/home/env_setup.sh"]
#CMD ["/sbin/tini", "--", "/usr/local/bin/jenkins.sh"]
