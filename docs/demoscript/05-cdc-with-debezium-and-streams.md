
## CDC with Debezium and Redis Streams

Some applications wants to react to transactions for multiple reasons:

* capture business events and process them for a specific new use case
* refresh data into a cache
* push data into a different datamodel (graph) to query/process them differently

You can find many different CDC (Change Data Catpure) tools on the market. This demonstration use Debezium to capture MySQL transaction.

To provide a very flexible architecture and decouple the event source (RDBMS transactions) each event will be sent to a Redis Streams. Then developers can create new services (sink) with the events.

### Script

1. Go to RedisInsight, as you can see in the streams screen you do not have any yet
2. Go to the `Services` in the sample application
3. Click start on the first step at the left. This will start the process that capture all transactions and push them into Redis Streams
4. Go back to RedisInsight and refresh the Streams page, you will see 3 streams, one by MySQL table: Movies, Actors and Theaters (not used in this app)
5. Look at the last message of the `"events:inventory:actors"` streams. (add more Colums)
6. You can see that all transactions are send as simple message
7. Go to the Actor screen inthe demonstration and modify the first name of an actor, and click save. (this will modify the record in MySQL)
8. Go back to RedisInsight, and you can see that a new message has been send to the streams. 


Let's now consume the messages to build new datamodel/store data into Redis.

---
Next: [Consume Streams and push data into Redis Hashes](06-consume-streams-to-redish-hash.md)