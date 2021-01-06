package io.redis.demos.services.db.events.streams.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.StreamEntryID;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class RedisStreamsDebeziumProducer {

    @Value("${redis.host}")
    private String redisHost;

    @Value("${redis.port}")
    private int redisPort;

    @Value("${redis.password}")
    private String redisPassword;

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
        log.info("Create Jedis Pool with {}:{} ", redisHost, redisPort);
        if (redisPassword != null && redisPassword.trim().isEmpty()) {
            redisPassword = null;
        }
        jedisPool = new JedisPool(new JedisPoolConfig(), redisHost, redisPort, 5000, redisPassword );

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

        // if update create addition information with "before" changes fields
        if ( cdcEvent.containsKey("before") ) {
            Map<String,Object> bodyBefore = (Map<String, Object>) cdcEvent.get("before");

            Map<String,String> beforeAsString = bodyBefore.entrySet().stream()
                    .filter(m -> m.getKey() != null && m.getValue() !=null)
                    .collect(Collectors.toMap(
                            e ->  { return "before:"+ e.getKey(); } ,
                            e -> e.getValue().toString())
                    );
            message.putAll(beforeAsString);
        }

        // Connect to Jedis and invalidate the key
        try (Jedis jedis = jedisPool.getResource()) {
            String key = "events:"+ db +":"+ table ;
            jedis.xadd( key, StreamEntryID.NEW_ENTRY, message );
        }
    }

}
