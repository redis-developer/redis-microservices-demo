package io.redis.demos.debezium.streams.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class ConnectorConfig {

    /**
     * Student Database details.
     */
    @Value("${database.hostname}")
    private String databaseHostname;

    @Value("${database.name}")
    private String databaseName;

    @Value("${database.port}")
    private String databasePort;

    @Value("${database.user}")
    private String databaseUser;

    @Value("${database.password}")
    private String rdbmsDBPassword;

    @Value("${database.password}")
    private String databasePassword;

    @Value("${database.server.id}")
    private String databaseServerId;

    @Value("${database.server.name}")
    private String databaseServerName;

    @Value("${table.whitelist}")
    private String tableWhitelist;


    @Bean
    public io.debezium.config.Configuration createConnectorConfig() {
        Properties props = new Properties();

        props.setProperty("name", "streamsserviceengine");
        props.setProperty("connector.class",  "io.debezium.connector.mysql.MySqlConnector");

        props.setProperty("database.hostname", databaseHostname);
        props.setProperty("database.name", databaseName);
        props.setProperty("database.port", databasePort);
        props.setProperty("database.user", databaseUser);
        props.setProperty("database.password", databasePassword);
        props.setProperty("database.server.id", databaseServerId);
        props.setProperty("database.server.name", databaseServerName);
        props.setProperty("database.history", "io.debezium.relational.history.FileDatabaseHistory");
        props.setProperty("database.history.file.filename", "./dbhistory-cdc-sync.dat");
        props.setProperty("table.whitelist", tableWhitelist);

        props.setProperty("offset.storage", "org.apache.kafka.connect.storage.FileOffsetBackingStore");
        props.setProperty("offset.storage.file.filename", "./offsets.dat");
        props.setProperty("offset.flush.interval.ms", "5000");

        return io.debezium.config.Configuration.from(props);
    }



}
