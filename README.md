# ssh-port-forward-study

## requirement

* Java 8 JDK
* Docker
* sbt
* node

## usage

before run test

```sh
docker-compose up
```

### groovy

```sh
cd groovy
./gradlew clean test
```

### scala

```sh
cd scala
sbt test
```

### node

```sh
cd node
npm test
```
