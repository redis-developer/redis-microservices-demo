
## Consume Streams and push data into Redis Hashes

Let's now consume the messages from the streams and create/update entries in Redis Hashes indexed using RediSearch.

### Script

1. Go to the Services screen
2. Start the  "Redis Streams to RediSearch/Hashes"
3. You have now many hashes in Redis, go to RedisInsight and you will see the new keys.
4. Actors, and Movies are indexed and you can now do queries on it:
    1. Click on Search, run some queries:
        
        `*`

        `star war`

        `star war -jedi`
5. Click on Movies (faceted) to look at the benefits of the fast search
6. You can copy the query from the screen in RedisInsight CLI or Search screen.

Other queries:
```
FT.SEARCH ms:search:index:movies "wars -Strip -Sith" RETURN 1 title
```

```
FT.SEARCH ms:search:index:movies "redis " RETURN 2 title plot
```

```
FT.AGGREGATE ms:search:index:movies: "@release_year:2015" GROUPBY 1 @genre REDUCE COUNT 0 AS sum SORTBY 2 @genre ASC MAX 10
```

The aggregation query is used in 2 places in the demonstration:

* in the "Statistics" screen
* in the list of categories with the number of movies in the Faceted Search screen


You can learn more about Redis query and indexing capabilities in the [RediSearch tutorial](https://github.com/RediSearch/redisearch-getting-started/blob/master/docs/001-introduction.md).

---
Next: [Consume Streams and push data into RedisGraph](07-consume-streams-to-redish-graph.md)