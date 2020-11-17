FROM openjdk:8-jdk-alpine

COPY target/*.jar /app.jar

ENV REDIS_URI=redis://redis-service:6379


EXPOSE 8085
ENTRYPOINT ["java","-jar","/app.jar","--spring.profiles.active=prod"]


