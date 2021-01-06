# Microservices with Redis

This project shows how you can modernize a legacy application that use RDBMS with Redis.

* Caching: Call Web Service and cache the result in Redis
* CDC to RediStreams: Capture MySQL transactionsd and send them to Redis Streams
* Use RediSearch to index relational data and provides autocomplete feature
* Store and Index data in Redis: copy the events from Streams and put them in a Redis Hash (Movie/Actors), this could be used as a cache or maine datastore 
* See how to run queries on Redis hashes using RediSearch commands (filter, sort, aggregate, and tull text search)
* Extend MySQL Legacy model with movie comments store in Hash and queried using RediSearch commands
* Push relationnoal data into RedisGraph
* a Web frontend developped with Vue.js

![Archi](./ui-redis-front-end/redis-front/public/imgs/overal-archi.png)


If you want to use the Web Service cache demo that call the OMDB API you must:

1. Generate a key here: [http://www.omdbapi.com/](http://www.omdbapi.com/) *do not forge to activate it, you will receive an email)

2. When the applications is ready go to the "Services" page and enter the key in the configuration screen, this will save the key in a Redis Hash (lool at `ms:config` during the demo)


## Build and Run with Docker


```
$ mvn clean package

$ docker-compose up --build

```

Cleanup

```

$ docker-compose down -v --rmi local --remove-orphans

```

## Deploy to Kubernetes

You can also deploy the application to Kubernetes, see [Kubernetes Readme](./kubernetes/README.md)