FROM openjdk:8-jdk-alpine

COPY target/*.jar /app.jar

ENV REDIS_HOST=redis-service
ENV REDIS_PORT=6379
ENV REDIS_PASSWORD=

ENV DATABASE_HOSTNAME=app-mysql
ENV DATABASE_PORT=3306
ENV DATABASE_USER=debezium
ENV DATABASE_PASSWORD=dbz

EXPOSE 8082
ENTRYPOINT ["java","-jar","/app.jar","--spring.profiles.active=prod"]


