FROM mysql:5.7

LABEL maintainer="Debezium Community & Tugdual Grall"

ENV MYSQL_ROOT_PASSWORD=debezium
ENV MYSQL_USER=mysqluser
ENV MYSQL_PASSWORD=mysqlpw

EXPOSE 3306

COPY mysql.cnf /etc/mysql/conf.d/
COPY import-data.sql /docker-entrypoint-initdb.d/


