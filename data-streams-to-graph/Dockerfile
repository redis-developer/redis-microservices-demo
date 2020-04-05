FROM openjdk:8-jdk-alpine

COPY target/data*.jar /app.jar

EXPOSE 8081
ENTRYPOINT ["java","-jar","/app.jar"]


