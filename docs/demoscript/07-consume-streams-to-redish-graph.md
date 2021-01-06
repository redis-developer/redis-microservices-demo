
## Consume Streams and push data into RedisGraph

Let's now consume the messages from the streams and create/update entries in RedisGraph.

### Script

1. Go to the Services screen
2. Start the  "Redis Streams to RediGraph"
3. The `imdb` graph is created and populated
4. Go to RedisInsight in the RedisGraph screen and run some queries


**Find the actor with `actor_id = 1`**

    ```
    MATCH (a:actor{actor_id:1}) RETURN a

    ```

    Click on the node to see the properties, right clic to select the attribute to show by default.

    Double click to navigate in movies, actor, ...



**Find the actor_id = 1 and all the movies he has acted in**

    ```
    MATCH (a:actor{actor_id:1})-[:acted_in]->(m:movie)  RETURN a,m
    ```


**Find all movies with Tom Cruise**

    ```
    MATCH (a:actor{first_name:'Tom', last_name:'Cruise'})-[r:acted_in]->(m:movie) RETURN m,a

    ```

**Count the number of movies by actors ('join')**

    ```
    MATCH (a:actor)-[:acted_in]->(m:movie) RETURN a.first_name, a.last_name, count(*) AS movies order by movies DESC
    ```




**Find actors and movies that played with Tom Cruise**

    ```
    MATCH (a:actor)-[:acted_in]->(m:movie),
    (other:actor)-[:acted_in]->(m) 
    WHERE a.last_name = 'Cruise' 
    AND other.last_name <> 'Cruise' 
    RETURN other.first_name, other.last_name, m.title
    ```

    
    