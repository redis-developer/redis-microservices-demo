package io.redis.demos.autocomplete;

import io.redisearch.Suggestion;
import io.redisearch.client.AutoCompleter;
import io.redisearch.client.Client;
import io.redisearch.client.SuggestionOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.*;
import redis.clients.jedis.exceptions.JedisDataException;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Component
public class RediStreamsToAutocomplete {

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

    // Autocomplete (keys)
    @Value("${redis.autocompletekeys}")
    private List<String> autoCompleteKeys;

    Future<?> streamProcessingTask;
    private String status = "STOPPED";
    private JedisPool jedisPool;
    private Map<String, Client> searchClients = new HashMap<>();
    private Map<String, String> indexNameByType = new HashMap<>();


    public RediStreamsToAutocomplete() {
        log.info(" === RediStreamsToAutocomplete Started : Waiting for user action  ===");
    }

    @PostConstruct
    private void afterConstruct(){
        try {
            log.info("Create Jedis Pool with {} ", redisUri);
            URI redisConnectionString = new URI(redisUri);
            jedisPool = new JedisPool(new JedisPoolConfig(), redisConnectionString);

            // create a search client for each key
            autoCompleteKeys.forEach( key -> {
                Client c = new Client( key, jedisPool );
                searchClients.put(key, c);




            });
        } catch (URISyntaxException use) {
            log.error("Error creating JedisPool {}", use.getMessage());
        }
        log.info("Will look at {} streams", streamList);
    }

    public Map<String,String> processStream() {
        Map<String, String> result =  new HashMap<>();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        streamProcessingTask = executor.submit(this::processStreamInThread);
        result.put("msg", "Stream Reading started");
        status = "RUNNING";
        return result;
    }

    /**
     * Stop the stream reader
     * @return
     */
    public Map<String,String> stopProcessStream(){
        log.info("Stopping Stream Processing");
        Map<String, String> result =  new HashMap<>();
        if (streamProcessingTask != null) {
            streamProcessingTask.cancel(true);
            result.put("msg", "Stream Reading Stopped");
        }
        return result;
    }

    public String getState() {
        return this.status;
    }

    /**
     * TODO: Make this generic to support dynamic service binding
     *       for new datatype based on stream name
     */
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

                    StreamEntryID streamEtnryId = new StreamEntryID("0-0");


                    Map.Entry<String, StreamEntryID> queryStream =
                            new AbstractMap.SimpleImmutableEntry<>( stream, StreamEntryID.UNRECEIVED_ENTRY);
                    xReadQueries[streamCtr[0]] = queryStream;
                    streamCtr[0]++;

                    try {
                        // Create consumer if does not exist already
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
                        loop = !streamProcessingTask.isCancelled();

                        // consume messages
                        List<Map.Entry<String, List<StreamEntry>>> events = jedis.xreadGroup(
                                groupName,
                                consumer,
                                1,
                                0,
                                false,
                                xReadQueries
                        );

                        if (events != null) {
                            for (Map.Entry m : events) {
                                if (m.getValue() instanceof ArrayList) {
                                    List<StreamEntry> l = (List) m.getValue();
                                    if (l.size() != 0) {

                                        Map<String, String> data = l.get(0).getFields();

                                        String operation = data.get("source.operation").toUpperCase();

                                        // create/update movie
                                        if (data.get("source.table").equalsIgnoreCase("movies")) {

                                            Map<String, Object> movie = new HashMap<>();
                                            movie.put("title", data.get("title").toString());
                                            movie.put("genre", data.get("genre").toString());
                                            movie.put("votes", Integer.parseInt(data.get("votes")));
                                            movie.put("rating", Float.parseFloat(data.get("rating")));
                                            movie.put("year", Integer.parseInt(data.get("release_year")));
                                            movie.put("movie_id", Integer.parseInt(data.get("movie_id")));

                                            if (operation.equals("UPDATE")) {
                                                movie.put("before:title", data.get("before:title"));
                                                movie.put("before:genre", data.get("before:genre"));
                                                movie.put("before:votes", Integer.parseInt(data.get("before:votes")));
                                                movie.put("before:rating", Float.parseFloat(data.get("before:rating")));
                                                movie.put("before:year", Integer.parseInt(data.get("before:release_year")));
                                            }


                                            // Prepare the indexing call
                                            String suggestionKey = "search:movies";
                                            String newValue = null;
                                            String oldValue = null;
                                            String id = movie.get("movie_id").toString();
                                            if (operation.equals("CREATE")) {
                                                newValue =  movie.get("title").toString();
                                            } else if (operation.equals("UPDATE")) {
                                                newValue =  movie.get("title").toString();
                                                oldValue =  movie.get("before:title").toString();
                                            } else  if (operation.equals("DELETE")) {
                                                oldValue =  movie.get("title").toString();
                                            }

                                            this.updateSearch(suggestionKey,newValue, oldValue, id, false);
                                            jedis.xack(m.getKey().toString(), groupName, l.get(0).getID());


                                        }
                                        if (data.get("source.table").equalsIgnoreCase("actors")) { // create/update actor
                                            Map<String, Object> actor = new HashMap<>();
                                            actor.put("first_name", data.get("first_name"));
                                            String lastName = data.get("last_name");
                                            if (lastName == null || lastName.trim().isEmpty()) {
                                                lastName = "-";
                                            }
                                            actor.put("last_name", lastName);
                                            actor.put("dob", Integer.parseInt(data.get("dob")));
                                            actor.put("actor_id", Integer.parseInt(data.get("actor_id")));
                                            actor.put( "full_name",  String.format("%s %s", data.get("first_name"), lastName ));

                                            if (operation.equals("UPDATE")) {
                                                String beforeLastName = data.get("before:last_name");
                                                if (beforeLastName == null || beforeLastName.trim().isEmpty()) {
                                                    beforeLastName = "-";
                                                }
                                                actor.put("before:last_name", beforeLastName);
                                                actor.put("before:dob", Integer.parseInt(data.get("before:dob")));
                                                actor.put("before:actor_id", Integer.parseInt(data.get("before:actor_id")));
                                                actor.put( "before:full_name",  String.format("%s %s", data.get("before:first_name"), beforeLastName ));
                                            }

                                            // Prepare the indexing call
                                            String suggestionKey = "search:actors";
                                            String newValue = null;
                                            String oldValue = null;
                                            String id = actor.get("actor_id").toString();
                                            if (operation.equals("CREATE")) {
                                                newValue =  actor.get("full_name").toString();
                                            } else if (operation.equals("UPDATE")) {
                                                newValue =  actor.get("full_name").toString();
                                                oldValue =  actor.get("before:full_name").toString();
                                            } else  if (operation.equals("DELETE")) {
                                                oldValue =  actor.get("full_name").toString();
                                            }

                                            this.updateSearch(suggestionKey,newValue, oldValue, id, false);
                                            jedis.xack(m.getKey().toString(), groupName, l.get(0).getID());

                                        }
                                    }
                                }
                            }
                        } else {
                            log.debug("no event in stream - ");
                            try {
                                TimeUnit.MILLISECONDS.sleep(500);
                            } catch (InterruptedException e1) {
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
                status = "STOPPED";
            }
        }
    }

    /**
     * Update Redis either a full text document or simply suggestion
     * @param newValue the string to index, null when delete
     * @param oldValue the string to remove or remove
     * @param fulltext
     */
    private void updateSearch(String index, String newValue, String oldValue, String id,  boolean fulltext ) {
        Client search = searchClients.get(index);

        if ( newValue != null && oldValue == null  ) { // creation
            log.info("CREATE FTS entry for {} in {}", newValue, index);
            Suggestion suggestion = Suggestion.builder().str(newValue)
                    .payload(id)
                    .build();
            search.addSuggestion(suggestion, true);
        } else if ( newValue != null && oldValue != null ) { // update

            log.info( "old {} / new {}", oldValue, newValue ) ;
            if (oldValue.equals(newValue) ) {
                log.info("UPDATE FTS Old and New values ({}) are identical, no update of the index {}", newValue, index);
            } else{
                log.info("UPDATE FTS entry for {} in {}", newValue, index);
                // TODO : wait for https://github.com/RediSearch/JRediSearch/issues/89
                Jedis jedis = null;
                try {
                    jedis = jedisPool.getResource();
                    Suggestion suggestion = Suggestion.builder().str(newValue).payload(id).build();
                    search.addSuggestion(suggestion, true);
                    Object result = jedis.sendCommand(AutoCompleter.Command.SUGDEL, index, oldValue);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
            }
        } else if ( newValue == null && oldValue != null ) { // delete
            log.info("DELETE FTS entry for {} in {}", oldValue, index);
            // TODO : wait for https://github.com/RediSearch/JRediSearch/issues/89
            Jedis jedis = null;
            try {
                jedis = jedisPool.getResource();
                Object result = jedis.sendCommand(AutoCompleter.Command.SUGDEL, index, oldValue  );
            } catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                if (jedis != null) {
                    jedis.close();
                }
            }
        }
    }

    /**
     * Return the suggestion based on terms
     * @param term Search index
     * @return
     */
    public List<Map<String,Object>> suggest(String indexName, String term){
        List<Map<String,Object>> result = new ArrayList<>();
        String complexIndexName = "search:" + indexName; // TODO: hard coded values....
        log.info("Suggestion query on {} with {}", complexIndexName, term);
        Client search = searchClients.get(complexIndexName);

        if (search != null) {
            List<Suggestion> suggestions = search.getSuggestion(term, SuggestionOptions.builder().max(100).fuzzy().with(SuggestionOptions.With.PAYLOAD).build() );

            result = suggestions
                    .stream()
                    .map( s -> {
                        Map<String,Object> m = new HashMap<>();
                        m.put("string", s.getString());
                        m.put("id", s.getPayload());
                        m.put("score", s.getScore());
                        return m;
                    } )
                    .collect(Collectors.toList());
        }
        return result;
    }



}
