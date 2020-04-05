FROM openjdk:8-jdk-alpine

COPY target/cache-*-SNAPSHOT.jar /app.jar

EXPOSE 8084
ENTRYPOINT ["java","-jar","/app.jar","--spring.profiles.active=prod"]


