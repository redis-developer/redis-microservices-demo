FROM mysql:5.7

LABEL maintainer="Debezium Community & Tugdual Grall"

COPY mysql.cnf /etc/mysql/conf.d/
COPY import-data.sql /docker-entrypoint-initdb.d/


