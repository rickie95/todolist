[![Coverage Status](https://coveralls.io/repos/github/rickie95/todolist/badge.svg?branch=master)](https://coveralls.io/github/rickie95/todolist?branch=master) [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=com.riccardomalavolti.apps%3Atodolist&metric=alert_status)](https://sonarcloud.io/dashboard?id=com.riccardomalavolti.apps%3Atodolist) [![Build Status](https://travis-ci.org/rickie95/todolist.svg?branch=master)](https://travis-ci.org/rickie95/todolist)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=com.riccardomalavolti.apps%3Atodolist&metric=code_smells)](https://sonarcloud.io/dashboard?id=com.riccardomalavolti.apps%3Atodolist)[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=com.riccardomalavolti.apps%3Atodolist&metric=sqale_index)](https://sonarcloud.io/dashboard?id=com.riccardomalavolti.apps%3Atodolist)

# todolist
A GUI todo list developed with Maven, Jenkins and a lot of friends.

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
UI views and controls. + Tests via AssertJ Swing. *This module requires a graphic server.*

#### todolist-tests-integration
Integration tests performed with components of both `core` and `ui`.

#### todolist-tests-e2e
End to end tests performed with Cucumber.

Note: you'll need Docker installed on your machine for building both `integration` and `e2e`.


### About the build

Multiple Maven profiles are available (use `-Pprofile1,profile2` to enable them):
  
  - **jacoco**: create a coverage report for `core` and `ui` modules. Those two reports are then fused in one during the execution of `jacoco-report-aggregator`.
  - **mutation-testing**: performs a mutation on `core` code and his tests.
  - **code-analysis**: sends data to Coveralls and Sonarcloud services.


## Jenkins CI container

A fully CI experience, with coverage and JUnit reports for Unit/Integration/End-2-End Tests.

  - Clone the repo
  - Create a `.env` file with API tokens by Coveralls and Sonarcloud
  - `docker-compose up`
  - Visit `localhost:8080` for the Jenkins web interface and launch jobs.

## Maven container setup

Useful if you only want to check the build process.

  - Clone the repo
  - `cd testing-container`
  - Create a `.env` file with API tokens by Coveralls and Sonarcloud
  - `docker-compose up`

## Software

| Software | Version |
|----------|--------|
| Maven | 3.6.3 |
| Java | openJDk 1.8.252 |
| Xvfb | For Jenkins and Maven containers |
| tightVNC | For Travis CI build |

