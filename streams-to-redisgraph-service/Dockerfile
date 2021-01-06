FROM openjdk:8-jdk-alpine

COPY target/*.jar /app.jar

ENV REDIS_HOST=redis-service
ENV REDIS_PORT=6379
ENV REDIS_PASSWORD=

EXPOSE 8083
ENTRYPOINT ["java","-jar","/app.jar","--spring.profiles.active=prod"]


