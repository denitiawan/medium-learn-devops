# image base from docker-hub
FROM openjdk:8-jdk-alpine

# Volume location
VOLUME /tmp

# port image target
EXPOSE 8080

# add or copy file from target folder
ADD target/app-0.1.jar app-0.1.jar

# syntax for run application file
ENTRYPOINT ["java","-jar","/app-0.1.jar"]
