# Dockerizing Jenkins

## Prelude
This tutorial is meant to illustrate a step-by-step creation of a Jenkins container able to reproduce a build of [todolist](https://ww.github.com/rickie95/todolist).

*On the host machine, Docker and Docker Compose must be installed in order to follow this tutorial* 

## The Jenkins Docker image

Jenkins is available in multiple Docker images, and we can use one of them for have a stable and tested environment ready to customize for our porpouses.

-   _jenkins_, the latest weekly update.
-   _jenkins:lts_, a more stable version. 
-   _jenkins:alpine_, a lightweight version. This version requires only half the space of a normal Jenkins image (~150Mb).

In this tutorial we'll use the _lts-alpine_ version, since we need to do very few customizations. So, our first row in `jenkins.dockerfile` states that we use such version.

``` FROM jenkins/jenkins:lts-alpine ```

Now, Jenkins interacts with the user through a web interface, so we need to open the container's `8080` door so that when visiting `localhost:8080` we will greeted by the initial screen. The `EXPOSE` directive will do that, so let's add this to the dockerfile.

```EXPOSE 8080```

In order to complete our bare container we also need to start Jenkins. The Docker image also contains a script ready to be executed.

``` ENTRYPOINT ["/sbin/tini", "--", "/usr/local/bin/jenkins.sh"] ```

**Tini** is a `init` replace for Docker containers: since our Jenkins instance will execute arbitrary code (like ours) it cannot be responsible for managing zombie processes. Tini registers itself with PID 1 and handles termination signals in a transparent mode to the software inside the container. You can find a very detailed and motivated explanation in [its repository](https://github.com/krallin/tini/issues/8).

So, now we should be able to build and start our `jenkins-lts-alpine` image.

```docker -f jenkins.dockerfile build . -t jenkins-alpine-lts```


```docker run -t jenkins-lts-alpine```

If you visit `localhost:8080` you should be greeted with something like this.

    ![Jenkins' dashboard](./resources/jenkins-dashboard.png)

Now that we have a working image of Jenkins we can start to customize it to our needs.

## Adding some basic tools

Since we're going to build a non-trivial Java application we need Maven. We also add some plugins to Jenkins and we set a dedicated user in order to avoid to execute it with user `root`.

First of all, we need to perform system-wide operations with `root` user. So, right after the `EXPOSE` row, we add:

```USER root```

We can now install some packages, `apk` is Alpine's package manager. We also set the enviroment variable for Maven's path.

    RUN apk --update --no-cache add maven=3.6.3 sudo
    ENV M2_HOME /usr/bin

Then we create a new user `jenkins` with admin privileges and we switch to him.

    RUN echo "jenkins ALL=NOPASSWD: ALL" >> /etc/sudoers
    USER jenkins

We might need to install some Jenkins plugins for our project. Inside `plugins.txt` we can list what we need, specifing the required version if we want. Below there's an example of its content.

    git:latest
    jacoco:latest
    code-coverage-api:latest
    junit:latest
    xvfb:latest
    ....

This file will be copied in our Jenkins' directory, then we execute a script which will download and install all the plugins listed.

    COPY jenkins-files/plugins.txt /var/jenkins_home/
    RUN /usr/local/bin/install-plugins.sh < /var/jenkins_home/plugins.txt

Our final goal is to have a ready-to-fire CI environment, so we don't want to startup our container and deal with jenkins' configurations. In this case we copy three `.xml` files:

- `config.xml` which contains settings and parameters for Jenkins. (e.g. the number of executors)
- `hudson.tasks.Maven.xml` and `org.jenkinsci.plugins.xvfb.Xvfb.xml`, containing paths to Maven and Xvfb binaries, basically.

We add those lines in our Dockerfile, after the plugins installation:

    COPY jenkins-files/config/*.xml /usr/share/jenkins/ref/
    RUN echo 2.0 > /usr/share/jenkins/ref/jenkins.install.UpgradeWizard.state

The last line prevents Jenkins to show up the plugin wizard on first run.

Now we should have a fully functional Jenkins container, ready to accept and execute jobs.

## The Pipeline Job

Now we can create a new job, which will build, test and perform code analysis on our application.

We need two files:

- `Jenkinsfile`: it's the real job, with stages and step. This file must be placed in your application's repository/folder.
- `myapplication-pipeline.xml`: this contains some metadata for our pipeline, such as the git repository url, the branch to checkout and the `Jenkinsfile` path in the repository base directory. See the repository of this project for a complete view.

In our dockerfile, we add this line and the `.xml` file will be copied in Jenkins' home folder.

    COPY jenkins-files/myapplication-pipeline.xml /usr/share/jenkins/ref/jobs/myapplication-pipeline/config.xml

Then, when we'll start our job, Jenkins will automatically fetch the required branch from the repository and execute the Pipeline specified in `Jenkinsfile`.

### The Jenkinsfile

Jenkins' pipelines are a powerfoul tool, but we'll use only a limited set of their potential.

The whole job is partitioned in *Stages*, every *Stage* has a name and executes one or more *Steps*.
A *step* can be a shell command, a program, a pipeline's scope routine, a plugin call, ecc..

For our needs, we've designed a 4-stage pipeline, mocking Maven's goals:

- *Build*: where we reach `clean` and `compile` goals.
- *Unit Testing*: we start an Xvfb instance since we need a graphic environment in order to test the UI components. The `test` phase is surrounded by a `wrap` directive which enables the Xvfb plugin. The virtual frame buffer is automatically closed after the step's ending.
 In this stage we perform coverage and test results collection using `Coverage API` and `JUnit` plugins. This allow us to visualize reports in the execution brief.
- *Integration and E2E testing*: Same as above, in this case we execute integration tests with the aid of Docker containers with MongoDB image. See `Docker-in-Docker` section to understand how to spawn Mongo containers alongside (instead of inside) Jenkins' container.
- *Code Analysis*: In this phase coverage and test reports are send to Coveralls and SonaCloud services to performs quality checks. In order to send results you need the API token for both services. Create an `.env` file and do not add it to Git index.

## Docker-in-Docker

Our integration test requires a database instance, such as MongoDB, in order to verify the correctness of methods that writes and reads from a real database.

Since we need to have a fresh copy of the db for every test it's natural to use docker but we need some special setting in order to avoid undesired surprised. It might be natural and immediate install Docker in our container and spawn MongoDB images *inside* as childs. However, in particular circumnstances, this might produce errors, even memory corruption. See [this Stack Overflow topic](https://devops.stackexchange.com/questions/676/why-is-docker-in-docker-considered-bad), or even better [get motivation from Docker-in-Docker ideator himself](https://jpetazzo.github.io/2015/09/03/do-not-use-docker-in-docker-for-ci/).

Given the (low) complexity of our CI setup we could avoid simply forget this issue, but the best practice is so simple that totally worth the 5 minute extra.

### Pulling out `docker-compose`

If we want to create "siblings" containers for our Jenkins instance we need to bind the Docker's socket of our host machine to the Jenkins' container. We could use the argument `--volume` during the launch but `docker-compose` offers some other functionalities that will be useful in a second time. Install it on your host machine and create a new `docker-compose.yml` file.

    version: 3.7
    services:
        jenkins:
            build:
                context: ./
                dockerfile: # relative path to jenkins dockerfile
            image: # image name
            env_file:
                - variables.env
            environment:
                DOCKER_SOCKET: /var/run/docker.sock
                DOCKER_HOST: unix://var/run/docker.sock
            volumes:
                - /var/run/docker.sock:/var/run/docker.sock
            network_mode: "host"

In the `volumes` section the container docker socket has been bound to the host's one. This allow the docker instance inside our Jenkins container to use the host's docker daemon so that it can create its siblings containers. However this is not enough, `jenkins` user might not be in the host `docker` group then it could not have read/write/exec permissions.

A simple shell script `docker-jenkins.sh` will solve the problem: 

1. We read the group identifier (GID) of docker host socket.
2. We create a new group `docker-host` inside our container and we assign it the GID we read before.
3. We add `jenkins` user to `docker-host`

This operation must be done before starting Jenkins, so we need to modify `jenkins.dockerfile` entrypoint row:

    ENTRYPOINT ["/sbin/tini", "--", "/usr/local/bin/docker-jenkins.sh"]

Since it's our entrypoint, `docker-jenkins` will also start Jenkins executing `/usr/local/bin/jenkins.sh`.

Pay attention to the `network` section: it has been changed to `host` since we need that Jenkins container must be able to reach MongoDB on host's ports. See the diagram below to understand the differences.

    ![Jenkins container with default network and host network](./resources/jenkins-diagram.png)

The `variables.env` contains SonarCloud and Coveralls tokens:

    SONARCLOUD_TOKEN=<your token here>
    COVERALLS_REPO_TOKEN=<your token here>

If you start your Jenkins service with `docker-compose up`, the container will bind to the 8080 port and you shuld be able to complete the pipeline job.