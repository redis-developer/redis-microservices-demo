FROM openjdk:8-jdk-alpine

COPY target/*.jar /app.jar

ENV REDIS_HOST=redis-service
ENV REDIS_PORT=6379
ENV REDIS_PASSWORD=
ENV MYSQL_ROOT_PASSWORD=debezium
ENV MYSQL_USER=mysqluser
ENV MYSQL_PASSWORD=mysqlpw

EXPOSE 8084
ENTRYPOINT ["java","-jar","/app.jar","--spring.profiles.active=prod"]


