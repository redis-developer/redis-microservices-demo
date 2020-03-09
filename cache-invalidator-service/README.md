# Redis Cache Invalidation Service

Demonstration:

[![](http://img.youtube.com/vi/UbIp92_CTCQ/0.jpg)](http://www.youtube.com/watch?v=UbIp92_CTCQ "CDC in Action")


### Building and Running the Service

#### 1- Start MySQL with Debezium sample dataset

Run this command to start MySQL in a Docker container.

```
$ docker run -it --rm --name mysql \
     -p 3306:3306 \
     -e MYSQL_ROOT_PASSWORD=debezium \
     -e MYSQL_USER=mysqluser \
     -e MYSQL_PASSWORD=mysqlpw \
     debezium/example-mysql:1.0
```

This container is configured to support [Debezium MySQL Connector](https://debezium.io/documentation/reference/1.0/connectors/mysql.html).


Connect to MySQL Command Line client:

```
$ docker run -it --rm --name mysqlterm \
     --link mysql \
     --rm mysql:5.7 \
     sh -c 'exec mysql -h"$MYSQL_PORT_3306_TCP_ADDR" -P"$MYSQL_PORT_3306_TCP_PORT" -uroot -p"$MYSQL_ENV_MYSQL_ROOT_PASSWORD"'
```

and run the following command: 

```json
mysql>  GRANT SELECT, RELOAD, SHOW DATABASES, REPLICATION SLAVE, REPLICATION CLIENT  ON *.* TO 'mysqluser'@'%';
Query OK, 0 rows affected (0.00 sec)


mysql> use inventory

mysql> SELECT * FROM customers;
```

(Note: I need to check, since it should not be necessary since it is present in the [SQL script](https://github.com/debezium/docker-images/blob/master/examples/mysql/1.1/inventory.sql#L12).)

#### 2- Start Redis is not already present

Let's start Redis, for example with Docker:

```
$ docker run -it --rm --name redis-server \
     -p 6379:6379 \
     redis
```

Connect to Redis using redis-cli, Redis Insight or any other tool. For example usind Docker:

```
$ docker exec -it redis-server redis-cli
```     


run the following command to check that the Redis database is empty:

```
127.0.0.1:6379> SCAN 0  COUNT 1000
```


#### 3- Run the Cache Invalidation Service

You if you are not using the default ports, and running the databases locally open 
the `./src/main/resources/application.properties` file and edit the properties.


Run the application using the following command:

```
$ mvn spring-boot:run
```

During the initial sync the content of the `inventory.customers` and `inventory.orders` is pushed into Redis.

Go back into `redis-cli`, and Scan the db again:

```
127.0.0.1:6379> SCAN 0  COUNT 1000
```

You can look into one of the key using:

```
127.0.0.1:6379> HGETALL inventory:customers:id:1001
```


**Update a MySQL Record**

In the MySQL CLI do run this update command:

```
mysql> UPDATE customers SET email='sally@redis-demo.com' WHERE id = 1001;
```

**Check Redis**

```
127.0.0.1:6379> HGETALL inventory:customers:id:1001
```

Email has been updated (in fact the whole Hash has been replaced)
 
 
 **Create a customer MySQL Record**
 
 In the MySQL CLI do run this update command:
 
 ```
 mysql> INSERT INTO customers VALUES (1005, 'John', 'Doe', 'jdoe@demo.com');
 ```

**Check Redis**

```
127.0.0.1:6379> HGETALL inventory:customers:id:1005
```

You see the new customer.

**Delete customer in MySQL**
 
In the MySQL CLI do run this update command:
 
 ```
 mysql> DELETE FROM customers WHERE id = 1005;
 ```

**Check Redis**

```
127.0.0.1:6379> HGETALL inventory:customers:id:1005
```
The key `inventory:customers:id:1005` is not present in Redis anymore.

----

You can change the configuration to:
* no save any data in Redis from the CDC Event, and only use Redis Service to delete updated/deleted keys `redis.setValue=false`
* only send data when a record is updated not created `redis.setOnInsert=false`