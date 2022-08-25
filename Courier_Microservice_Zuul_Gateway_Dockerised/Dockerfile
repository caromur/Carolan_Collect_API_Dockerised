FROM alpine
MAINTAINER Adam Carolan
RUN apk update
RUN apk add openjdk11
COPY target/zuulgateway-0.0.1-SNAPSHOT.jar Courier_Gateway.jar
ENTRYPOINT ["java","-jar","/Courier_Gateway.jar"]