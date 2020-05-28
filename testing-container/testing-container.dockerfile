FROM maven:3.6.3-jdk-8-slim

RUN apt-get update && apt-get -y install git docker tightvncserver expect

RUN rm -rf /var/lib/apt/lists/*

# Copy the maven build script
COPY maven-build-script.sh /usr/local/bin/start-maven-build.sh
COPY vnc-prompt-password.exp /var/vnc-prompt-password.exp
RUN chmod +x /usr/local/bin/start-maven-build.sh
CMD ["/usr/local/bin/start-maven-build.sh"]
