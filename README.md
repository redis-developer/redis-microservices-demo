# Microservices with Redis

This project shows how you can modernize a legacy application that use RDBMS with Redis.

* Caching: take some data out of RDBMS
* Use RediSearch to index relational data and provides autocomplete feature
* Use Redis Graph to provide a new way to navigate and use the data
* Build an event based architecture using Redis Streams


### 1- [Cache Invalidation](cache-invalidator-service)

This Spring Boot Application is a service that use Debezium in an embedded mode and listen to CDC event from MySQL.
Depending of the configuration, the table content is automatically cached as a hash or just invalidated based on the table primary key.




## Build and Run with Docker


If you want to use the Web Service cache demo that call the OMDB API you must:

1. Generate a key here: [http://www.omdbapi.com/](http://www.omdbapi.com/) *do not forge to activate it, you will receive an email)

2. When the applications is ready go to the "Services" page and enter the key in the configuration screen, this will save the key in a Redis Hash (lool at `ms:config` during the demo)



```
$ mvn clean package

$ docker-compose up --build

```

Cleanup

```

$ docker-compose down -v --rmi local --remove-orphans

```