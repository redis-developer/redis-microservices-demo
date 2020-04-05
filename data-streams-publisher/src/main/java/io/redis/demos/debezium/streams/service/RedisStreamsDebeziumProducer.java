package io.redis.demos.debezium.streams.service;

import io.debezium.data.Envelope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.StreamEntryID;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
public class RedisStreamsDebeziumProducer {

    // URI used to connect to Redis database
    @Value("${redis.uri}")
    private String redisUri;

    // Should the system set the value when a table is updated ?
    @Value("${redis.setValue}")
    private String setValue;

    // If setValue, should the system create new entry when a record is created
    @Value("${redis.setOnInsert}")
    private String setOnInsert;

    private JedisPool jedisPool;

    public RedisStreamsDebeziumProducer() {
    }

    @PostConstruct
    private void afterConstruct(){
        try {
            log.info("Create Jedis Pool with {} ", redisUri);
            URI redisConnectionString = new URI(redisUri);
            jedisPool = new JedisPool(new JedisPoolConfig(), redisConnectionString);
        } catch (URISyntaxException use) {
            log.error("Error creating JedisPool {}", use.getMessage());
        }
    }


    /**
     * When a new event is coming :
     *  - Extract the db, table and id of the record
     *  - Using redis key format db:table:id
     *  - Depending of the Redis plugins configuration:
     *     - Unlink, update or create the key (that will be a Hash)
     * @param cdcEvent
     */
    public void publishEventToStreams(Map<String, Object> cdcEvent) {

        Map<String,Object> header = (Map<String, Object>) cdcEvent.get("header");
        Map<String,Object> body = (Map<String, Object>) cdcEvent.get("body");

        String db = header.get("source.db").toString();
        String table = header.get("source.table").toString();

        Map<String,String> headerAsString = header.entrySet().stream()
                .filter(m -> m.getKey() != null && m.getValue() !=null)
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().toString()));

        Map<String,String> bodyAsString = body.entrySet().stream()
                .filter(m -> m.getKey() != null && m.getValue() !=null)
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().toString()));

        // merge the maps into a Map keeping the attribute in order for easy reading
        Map<String,String> message = new LinkedHashMap<>();
        message.putAll(headerAsString);
        message.putAll(bodyAsString);


        // Connect to Jedis and invalidate the key
        try (Jedis jedis = jedisPool.getResource()) {
            String key = "events:"+ db +":"+ table ;

            System.out.println(message);
            jedis.xadd( key, StreamEntryID.NEW_ENTRY, message );

        }
    }

}
