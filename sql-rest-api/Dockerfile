FROM openjdk:8-jdk-alpine

COPY target/*.jar /app.jar

ENV SPRING_DATASOURCE_URL=jdbc:mysql://app-mysql:3306/inventory
ENV SPRING_DATASOURCE_USERNAME=mysqluser
ENV SPRING_DATASOURCE_PASSWORD=mysqlpw

EXPOSE 8081
ENTRYPOINT ["java","-jar","/app.jar","--spring.profiles.active=prod"]


