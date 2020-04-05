FROM openjdk:8-jdk-alpine

COPY target/sql*.jar /app.jar

EXPOSE 8081
ENTRYPOINT ["java","-jar","/app.jar","--spring.profiles.active=prod"]


