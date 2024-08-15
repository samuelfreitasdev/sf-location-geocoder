# Smart Geocoder Inference Engine

Sample CVRP Application using Kotlin + Spring Boot + Apache Camel + Jooq

## What is?

This is a sample application that uses a markov chains to estimate coordinate among a list where the result has a better accuracy than the original data.

A common use case is when a deliverer has to deliver something in huge place as a Shopping or a University Campus. Using the common Geocoding APIs to convert the address to coordinates, the result is not accurate enough to delivery the product in the right place. To overcome that problem, this project proposes a solver to use a history of coordinates from previous events to determine an accurate coordinate.

### Project Modules

The project uses a [hexagonal architecture](https://jmgarridopaz.github.io/content/articles.html) and was broken into the following modules:

* geocoder-core: domain modules with main business logic;
* geocoder-repo: adapter module for relational database persistence using Jooq;
* geocoder-solvers: modules containing several solvers;
* geocoder-webcli: webclient app (SPA) using Vue3;
* geocoder-app: webserver application using Spring Boot.

The `geocoder-solvers` module contains several submodules that uses different solvers strategies implementations. They are:

* geocoder-markov-solver: A solver using Markov Chains;
* geocoder-gravitational-search: A solver using Gravitational Search Algorithm; **(in development)**

### Screenshots

Problems List

Solver View

### How to run

To run the project, you need to have a PostgreSQL database running. You can use the compose.yaml to start a docker container:

```shell

docker-compose -f compose.yaml up

```

After that, you can run the application using the following command:

```shell  

./gradlew :geocoder-app:bootRun

```
