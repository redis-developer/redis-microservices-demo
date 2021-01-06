package io.redis.demos.services.caching.service;

import io.debezium.data.Envelope.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RedisCacheService {

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

    public RedisCacheService() {
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
    public void updateRedis(Map<String, Object> cdcEvent) {

        System.out.println(cdcEvent);

        Map<String,Object> header = (Map<String, Object>) cdcEvent.get("header");
        Map<String,Object> body = (Map<String, Object>) cdcEvent.get("body");
        String db = header.get("source.db").toString();
        String table = header.get("source.table").toString();
        Operation operation = (Operation)header.get("source.operation");

        List<String> keyFields = (List<String>)header.get("source.key.fields");
        List keyValues = keyFields.stream().map(fieldName -> fieldName +":"+ body.get(fieldName).toString()).collect(Collectors.toList());

        String id = String.join(":", keyValues); // create unique id as redis Key

        // Connect to Jedis and invalidate the key
        try (Jedis jedis = jedisPool.getResource()) {
            String key = db +":"+ table +":"+ id;

            if ( ! setValue.equalsIgnoreCase("true")) {
                log.info("Invalidate cache value {}", key);
                jedis.unlink(key);
            } else {

                if(operation == Operation.DELETE ) {
                    log.info("Delete delete key {}", key);
                    jedis.unlink(key);

                } else {
                    if (operation == Operation.UPDATE ||
                        (operation == Operation.CREATE && setOnInsert.equalsIgnoreCase("true"))
                       ){
                        log.info("Update or create key {}}", key);
                        // Basic String conversion
                        Map<String,String> bodyAsString = body.entrySet().stream()
                                .filter(m -> m.getKey() != null && m.getValue() !=null)
                                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().toString()));

                        jedis.hset(key, bodyAsString);
                    }
                }
            }

        }
    }

}
