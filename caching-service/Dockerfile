FROM openjdk:8-jdk-alpine

RUN apk update && apk upgrade && \
  apk add --no-cache maven

COPY . .

RUN mvn package

RUN mv target/*.jar /app.jar

EXPOSE 8084
ENTRYPOINT ["java","-jar","/app.jar","--spring.profiles.active=prod"]


