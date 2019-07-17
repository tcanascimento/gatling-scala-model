FROM openjdk:8-jre-alpine

LABEL version="0.1" mantainer="tcanascimento@gmail.com" "description"="base image for gatling tests on k8s"

RUN apk update && apk add bash

WORKDIR /opt

ADD target/scala-2.12/load-test.jar /opt/load.jar

ENTRYPOINT ["java", "-jar", "load.jar"]