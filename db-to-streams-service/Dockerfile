FROM openjdk:8-jdk-alpine

COPY target/*.jar /app.jar

EXPOSE 8082
ENTRYPOINT ["java","-jar","/app.jar","--spring.profiles.active=prod"]


