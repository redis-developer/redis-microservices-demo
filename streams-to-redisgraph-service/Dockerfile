FROM openjdk:8-jdk-alpine

COPY target/*.jar /app.jar

EXPOSE 8083
ENTRYPOINT ["java","-jar","/app.jar","--spring.profiles.active=prod"]


