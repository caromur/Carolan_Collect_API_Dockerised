FROM alpine
MAINTAINER Adam Carolan
RUN apk add openjdk11
COPY target/Courier_Microservice-0.0.1-SNAPSHOT.jar Courier_Instance_Test_Container_MySQL.jar
COPY src/main/resources/Logo4.jpg src/main/resources/Logo4.jpg
ENTRYPOINT ["java","-jar","/Courier_Instance_Test_Container_MySQL.jar"]