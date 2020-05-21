# Blockchain Demonstration Tool

This project was presented as the final year project for the B.Sc (Hons) in Information Technology at CCT College Dublin.

It is a blockchain tool, that creates a node capable fo performing many different functions.

This project uses Quarkus, the Supersonic Subatomic Java Framework and Apache Artemis (ActiveMQ) messaging system.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the Apache Artemis in Docker

Before running this solution, a Apache Artemis server is needed.

It can be created by the following Docker command.

```
docker run -it --rm -p 8161:8161 -p 61616:61616 -p 5672:5672 -e ARTEMIS_USERNAME=blockmsg -e ARTEMIS_PASSWORD='pass1234!'  vromero/activemq-artemis:2.12.0-alpine
```

Note that USER and PASS can be changed before running.


## Running the application

When running the application with no environment variables, it uses the default information available.

It is a sample server deployed to EC2 and available for a couple of months after this solution was submitted.


## Environment variables
The system accepts a few environment variables to customise the application at runtime:
```
export BDT_PORT=8000
export MESSAGING_IP=54.85.47.98:61616
export MESSAGING_USER=blockmsg
export MESSAGING_PASS=pass1234!
```

### Using Maven and this repo to run the application
You can, then, run your application in dev mode that enables live coding, by cloning this repo and running the following command:
```
./mvnw quarkus:dev
```


### Using Docker to run the application

If the last stable solution is to be used, the following command allows its Docker image to be run:
```
docker run -i --rm gustavolessadublin/blockchain-tool
```

If env variables need to be used, a more complete command is available:

```
docker run -i --rm --env BDT_PORT=8079 MESSAGING_IP=54.85.47.98:61616 MESSAGING_USER=blockmsg MESSAGING_PASS=pass1234! gustavolessadublin/blockchain-tool
```



## Packaging and running the application

The application can be packaged using `./mvnw package`.
It produces the `blockchain-tool-1.0.0-SNAPSHOT-runner.jar` file in the `/target` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/lib` directory.

The application is now runnable using `java -jar target/blockchain-tool-1.0.0-SNAPSHOT-runner.jar`.

## Creating a native executable

You can create a native executable using: `./mvnw package -Pnative`.

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: `./mvnw package -Pnative -Dquarkus.native.container-build=true`.

You can then execute your native executable with: `./target/blockchain-tool-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/building-native-image.