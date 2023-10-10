FROM openjdk:17-jdk-slim-buster
VOLUME /tmp 
COPY target/productmgmt-0.0.1-SNAPSHOT.jar product-microservice.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/product-microservice.jar"] 
