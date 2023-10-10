FROM openjdk:17-jdk-slim-buster
VOLUME /tmp 
COPY target/ordermgmt-0.0.1-SNAPSHOT.jar order-microservice.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/order-microservice.jar"] 
