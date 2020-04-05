package io.redis.demos.debezium.streams.listener;

import io.debezium.config.Configuration;
import io.debezium.data.Envelope;
import io.debezium.embedded.Connect;
import io.debezium.engine.DebeziumEngine;
import io.redis.demos.debezium.streams.service.RedisStreamsDebeziumProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static io.debezium.data.Envelope.FieldName.*;
import static java.util.stream.Collectors.toMap;

@Slf4j
@Component
@org.springframework.context.annotation.Configuration
public class CDCEventListener {

    private String topicName;

    // Thread for the Debezium engine
    private final Executor executor = Executors.newSingleThreadExecutor();

    // Service layer to interact with Redis
    private final RedisStreamsDebeziumProducer redisStreamsService;
    private final Properties configAsProperties;

    public CDCEventListener(Configuration config, RedisStreamsDebeziumProducer service) throws IOException {
        topicName = config.getString("database.server.name") + "." + config.getString("database.name") + ".";
        redisStreamsService = service;
        configAsProperties = config.asProperties();
    }


    public void startDebezium() throws IOException {
        try (DebeziumEngine<SourceRecord> engine = DebeziumEngine.create(Connect.class).using(configAsProperties)
                .notifying(record -> {
                    handleEvent(record);
                }).build()) {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(engine);
        }

    }


    /**
     * Capture CDC Event if event is from the proper DB/Table send it to @RedisCacheService
     *
     * @param record
     */
    private void handleEvent(SourceRecord record) {

        // check of the events is sent to the proper topic that is the server-name and database
        if ( record != null && record.topic() != null && record.topic().startsWith(topicName)) {
            Struct payload = (Struct) record.value();

            if (payload != null && payload.getString("op") != null) {
                String tableName = null;
                String id = null;
                String structureType = AFTER;

                // prepare body
                Envelope.Operation op = Envelope.Operation.forCode(payload.getString("op"));
                if (op == Envelope.Operation.DELETE) {
                    structureType = BEFORE;
                }

                // prepare header
                Struct sourcePayload = (Struct) payload.get(SOURCE);
                Map<String, Object> cdcHeader = new HashMap<>();
                cdcHeader.put("source.db", sourcePayload.getString("db"));
                cdcHeader.put("source.table", sourcePayload.getString("table"));
                cdcHeader.put("source.operation", op);

                List<String> fieldNames = record.keySchema().fields().stream().map(field -> field.name()).collect(Collectors.toList());
                cdcHeader.put("source.key.fields", fieldNames);

                Struct messagePayload = (Struct) payload.get(structureType);
                Map<String, Object> cdcPayload = messagePayload.schema().fields().stream().map(Field::name)
                        .filter(fieldName -> messagePayload.get(fieldName) != null)
                        .map(fieldName -> Pair.of(fieldName, messagePayload.get(fieldName)))
                        .collect(toMap(Pair::getKey, Pair::getValue));

                // send a CDC event with header (db, table names and key fields) and body (values)
                Map<String, Object> cdcEvent = new HashMap<>();
                cdcEvent.put("header", cdcHeader);
                cdcEvent.put("body", cdcPayload);
                this.redisStreamsService.publishEventToStreams(cdcEvent);
            }
        }
    }
}


