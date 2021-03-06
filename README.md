[![Coverage Status](https://coveralls.io/repos/github/rickie95/todolist/badge.svg?branch=master)](https://coveralls.io/github/rickie95/todolist?branch=master) [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=com.riccardomalavolti.apps%3Atodolist&metric=alert_status)](https://sonarcloud.io/dashboard?id=com.riccardomalavolti.apps%3Atodolist) [![Build Status](https://travis-ci.org/rickie95/todolist.svg?branch=master)](https://travis-ci.org/rickie95/todolist)

[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=com.riccardomalavolti.apps%3Atodolist&metric=code_smells)](https://sonarcloud.io/dashboard?id=com.riccardomalavolti.apps%3Atodolist) [![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=com.riccardomalavolti.apps%3Atodolist&metric=sqale_index)](https://sonarcloud.io/dashboard?id=com.riccardomalavolti.apps%3Atodolist)

# todolist
A GUI todo list developed with Maven, Jenkins and all the gang.

## Project structure

    todolist
    +---> todolist-core
    +---> todolist-ui
    +---> todolist-tests-integration
    +---> todolist-tests-e2e
    +---> todolist-jacoco-report-aggregator

#### todolist-core
Basic functionality, backend logic, businness model, database/repositories. + Tests + Mutations (if enabled)

#### todolist-ui
UI views and controls. + Tests via AssertJ Swing.

#### todolist-tests-integration
Integration tests performed with components of both `core` and `ui`.

#### todolist-tests-e2e
End to end tests performed with Cucumber.

Note: you'll need Docker installed on your machine for building both `integration` and `e2e`.
Note #2: A graphic environment must be available in order to test `ui`, `test-integration` and `test-e2e` modules.


### About the build

Multiple Maven profiles are available (use `-Pprofile1,profile2` to enable them):
  
  - **jacoco**: create a coverage report for `core` and `ui` modules. Those two reports are then fused in one during the execution of `jacoco-report-aggregator`.
  - **mutation-testing**: performs a mutation on `core` code and his tests.
  - **code-analysis**: sends data to Coveralls and Sonarcloud services.

# Build reproducibility

You have multiple choices to test this project:

- **Jenkins' container**: Uses a instance of Jenkins to build, test and analyse the app, also providing a nice view for tests reports.
- **Testing-Container**: An Ubunutu Docker image with fixed versions of both Maven and JDK, UI tests are made possible by TightVNC.
- A **Travis CI instance** is available but for this repository only.

## Jenkins CI container

A fully CI experience, with coverage and JUnit reports for Unit/Integration/End-2-End Tests.

  - Clone the repo
  - Create a `.env` file with API tokens by Coveralls and Sonarcloud
  - `docker-compose up`
  - Visit `localhost:8080` for the Jenkins web interface and launch jobs.

## Testing-Container setup

Useful if you only want to check the build process.

  - Clone the repo
  - `cd testing-container`
  - Create a `.env` file with API tokens by Coveralls and Sonarcloud
  - `docker-compose up`

## Software

| Software | Version |
|----------|--------|
| Maven | 3.6.3 |
| Java | openJDK 1.8.252 |
| Xvfb | Used in Jenkins' container |
| tightVNC | Used for Travis CI build and Testing Container |

