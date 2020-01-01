FROM alpine:3.9
EXPOSE 8080

RUN apk --update add openjdk8-jre
RUN apk add sudo curl jenkins
RUN echo "jenkins ALL=NOPASSWD: ALL" >> /etc/sudoers
# Installs all the plugins listed in the .txt
# COPY plugins.txt /var/jenkins_home/plugins.txt
CMD ["/usr/bin/java", "-jar", "/usr/share/webapps/jenkins/jenkins.war"]
