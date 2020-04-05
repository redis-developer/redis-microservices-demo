# Debezium CDC & Redis

This project shows how to use Debezium together, for various use cases:

### 1- [Cache Invalidation](cache-invalidator-service)

This Spring Boot Application is a service that use Debezium in an embedded mode and listen to CDC event from MySQL.
Depending of the configuration, the table content is automatically cached as a hash or just invalidated based on the table primary key.


### 2. Extend your Relational Model with Redis Modules

Todo:

* show how to integrate RedisGraph and Search to enrich RDBMS application



## Build and Run with Docker

``
$ mvn clean package

$ docker-compose up --build

``

Cleanup

```json

$ docker-compose down -v --rmi local --remove-orphans

```