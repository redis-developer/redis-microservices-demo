package io.redis.demos.debezium.graph;

import com.redislabs.redisgraph.ResultSet;
import com.redislabs.redisgraph.impl.api.RedisGraph;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import redis.clients.jedis.*;
import redis.clients.jedis.exceptions.JedisDataException;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class RedisStreamToGraphService {

    // URI used to connect to Redis database
    @Value("${redis.uri}")
    private String redisUri;

    // Stream lists
    @Value("${redis.streams}")
    private List<String> streamList;

    // Stream lists
    @Value("${redis.groupname}")
    private String groupName;

    // Consumer name of this instance
    @Value("${redis.consumer}")
    private String consumer;

    // Graph name (key)
    @Value("${redis.graphname}")
    private String graphname;

    private JedisPool jedisPool;
    RedisGraph graph;

    @Autowired JdbcTemplate
            jdbcTemplate;

    public RedisStreamToGraphService() {
        log.info(" === RedisStreamToGraphService Started : Waiting for user action  ===");
    }

    @PostConstruct
    private void afterConstruct(){
        try {
            log.info("Create Jedis Pool with {} ", redisUri);
            URI redisConnectionString = new URI(redisUri);
            jedisPool = new JedisPool(new JedisPoolConfig(), redisConnectionString);
            graph = new RedisGraph(jedisPool);
        } catch (URISyntaxException use) {
            log.error("Error creating JedisPool {}", use.getMessage());
        }
        log.info("Will look at {} streams", streamList);

    }

    public Map<String,String> processStream() {

        Map<String, String> result =  new HashMap<>();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(this::processStreamInThread);
        result.put("msg", "Stream Reeading started");
        return result;

    }


    public void processStreamInThread() {
        log.info(" start stream processing");
        if (streamList.isEmpty()) {
            final String msg = "No stream to process";
            log.warn(msg);
        } else {
            final String msg = String.format("Will process %s ", streamList);
            log.warn(msg);


            Jedis jedis = null;
            try {
                jedis = jedisPool.getResource();

                Map.Entry<String, StreamEntryID>[] xReadQueries = (Map.Entry<String, StreamEntryID>[]) new Map.Entry[2];

                Jedis finalJedis = jedis;
                final int[] streamCtr = { 0 };
                streamList.forEach( stream -> {


                    Map.Entry<String, StreamEntryID> queryStream =
                            new AbstractMap.SimpleImmutableEntry<>( stream, StreamEntryID.UNRECEIVED_ENTRY);
                    xReadQueries[streamCtr[0]] = queryStream;
                    streamCtr[0]++;

                    try {
                        // Create consumer if does not exist already
                        StreamEntryID streamEtnryId = new StreamEntryID("0-0");
                        finalJedis.xgroupCreate(
                                stream,
                                groupName,
                                streamEtnryId,
                                true
                        );
                        log.info(" Consumer Group {} / {} created", stream, groupName);

                    } catch (JedisDataException jde) {
                        if (jde.getMessage().startsWith("BUSYGROUP")) {
                            log.info(" Consumer Group {} / {} already exists", stream, groupName);
                        }
                    }

                });

                boolean loop = true;

                try {

                while(loop) {


                    // consume messages
                    List<Map.Entry<String, List<StreamEntry>>> events = jedis.xreadGroup(
                            groupName,
                            consumer,
                            1,
                            0,
                            true,
                            xReadQueries
                    );


                    if (events != null) {
                        for (Map.Entry m : events) {
                            System.out.println(m.getKey() + " : " + m.getValue().getClass());
                            if (m.getValue() instanceof ArrayList) {
                                List<StreamEntry> l = (List) m.getValue();
                                Map<String, String> data = l.get(0).getFields();
                                for (Map.Entry entry : data.entrySet()) {
                                    System.out.println(entry.getKey() + " : " + entry.getValue());
                                }

                                // create/update movie
                                if ( data.get("source.table").equalsIgnoreCase("movies") ) {
                                    // save data into jedis graph
                                    Map<String,Object> movie = new HashMap<>();
                                    movie.put("title", data.get("title"));
                                    movie.put("genre", data.get("genre"));
                                    movie.put("votes", Integer.parseInt(data.get("votes")));
                                    movie.put("rating", Float.parseFloat(data.get("rating")));
                                    movie.put("year", Integer.parseInt(data.get("release_year")));
                                    movie.put("movie_id", Integer.parseInt(data.get("movie_id")));

                                    // TODO : manage update
                                    if ( data.get("source.operation").equalsIgnoreCase("CREATE") ) {
                                        graph.query(graphname, "MERGE (:movie{title:$title,genre:$genre,year:$year,votes:$votes,rating:$rating, movie_id:$movie_id})", movie);
                                        createRelationFromMySQL( "movie", Integer.parseInt(data.get("movie_id")));


                                        jedis.xack(streamList.get(0), groupName, l.get(0).getID());
                                        log.info("Merged movie '{}' in graph ", movie.get("title")  );
                                    } else  if ( data.get("source.operation").equalsIgnoreCase("DELETE") ) {
                                        graph.query(graphname,
                                                "MATCH (m:movie{movie_id:$movie_id}) DELETE m",
                                                movie);
                                        jedis.xack(streamList.get(0), groupName, l.get(0).getID());
                                        log.info( "DELETING {}",  movie.toString()  );                                    }


                                } if ( data.get("source.table").equalsIgnoreCase("actors") ) { // create/update actor
                                    Map<String,Object> actor = new HashMap<>();
                                    actor.put("first_name", data.get("first_name"));
                                    String lastName = data.get("last_name");
                                    if (lastName == null || lastName.trim().isEmpty() ) {
                                        lastName = "-";
                                    }
                                    actor.put("last_name", lastName  );
                                    actor.put("dob", Integer.parseInt(data.get("dob")));
                                    actor.put("actor_id", Integer.parseInt(data.get("actor_id")));

                                    // TODO : manage update
                                    if ( data.get("source.operation").equalsIgnoreCase("CREATE") ) {
                                        graph.query(graphname,
                                                "MERGE (:actor{first_name:$first_name, last_name:$last_name, dob:$dob , actor_id:$actor_id})",
                                                actor);

                                        createRelationFromMySQL( "actor", Integer.parseInt(data.get("actor_id")));

                                        jedis.xack(streamList.get(0), groupName, l.get(0).getID());
                                        log.info("Merged actor '{} {}' in graph - ", actor.get("first_name"), actor.get("last_name"));
                                    } else  if ( data.get("source.operation").equalsIgnoreCase("DELETE") ) {
                                        graph.query(graphname,
                                                "MATCH (a:actor{actor_id:$actor_id}) DELETE a",
                                                actor);
                                        jedis.xack(streamList.get(0), groupName, l.get(0).getID());
                                        log.info( "DELETING {}",  actor.toString()  );
                                    }

                                }

                            }
                        }
                    } else {
                        log.info("no event in stream - ");
                        try {
                            TimeUnit.MILLISECONDS.sleep(500);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                    }

                }
                } catch (JedisDataException jde) {
                    log.warn( jde.getMessage() );
                }



            } finally {
                if (jedis != null) {
                    jedis.close();
                }

                log.info("Leaving the stream processing method");
            }
        }

    }

    /**
     * Using a different approach for the relation,
     * we could use the streams approach, but I wanted to test with a queue
     *
     * This will generate too much queries for now
     */
    public void createRelationFromMySQL(String type, int id) {
        log.info( "\t find relationship for {} : {}", type, id );

        String queryString = "1=2"; // no row by default;
        // Quick & Dirty query builder
        if (type.equalsIgnoreCase("movie")) {
            queryString = " movie_id = "+ id;
        } else {
            queryString = " actor_id = "+ id;
        }

       List<Map<String, Object>>  actedIn = jdbcTemplate.queryForList("select * from movies_actors where "+ queryString );
       actedIn.forEach( record -> {
           int movieId = Integer.parseInt(record.get("movie_id").toString());
           int actorId = Integer.parseInt(record.get("actor_id").toString());
           Map<String, Object> params = new HashMap<>();
           params.put("movie_id",movieId);
           params.put("actor_id",actorId);
           try {
           ResultSet rs = graph.query( graphname,
                   "MATCH (m:movie{ movie_id:$movie_id}) MATCH (a:actor{ actor_id : $actor_id }) MERGE (a)-[r:acted_in]->(m)",
                   params
           );
           } catch (JedisDataException jde) {
               log.error(jde.getMessage());
           }
       });
    }

}
