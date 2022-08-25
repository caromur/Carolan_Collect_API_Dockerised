FROM alpine
MAINTAINER Adam Carolan
RUN apk update
RUN apk add openjdk11
COPY target/netflix-eureka-server-0.0.1-SNAPSHOT.jar Courier_Eureka.jar
ENTRYPOINT ["java","-jar","/Courier_Eureka.jar"]