FROM openjdk:8-jdk-alpine

RUN apk update && apk upgrade && \
  apk add --no-cache maven

COPY . .

RUN mvn package

RUN mv target/*.jar /app.jar

ENV REDIS_HOST=redis-service
ENV REDIS_PORT=6379
ENV REDIS_PASSWORD=

EXPOSE 8085
ENTRYPOINT ["java","-jar","/app.jar","--spring.profiles.active=prod"]


