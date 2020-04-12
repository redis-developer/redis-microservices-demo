# Microservices with Redis

This project shows how you can modernize a legacy application that use RDBMS with Redis.

* Caching: take some data out of RDBMS
* Use RediSearch to index relational data and provides autocomplete feature
* Use Redis Graph to provide a new way to navigate and use the data
* Build an event based architecture using Redis Streams


### 1- [Cache Invalidation](cache-invalidator-service)

This Spring Boot Application is a service that use Debezium in an embedded mode and listen to CDC event from MySQL.
Depending of the configuration, the table content is automatically cached as a hash or just invalidated based on the table primary key.


### 2. Extend your Relational Model with Redis Modules

Todo:

* show how to integrate RedisGraph and Search to enrich RDBMS application



## Build and Run with Docker

```
$ mvn clean package

$ docker-compose up --build

```

Cleanup

```

$ docker-compose down -v --rmi local --remove-orphans

```