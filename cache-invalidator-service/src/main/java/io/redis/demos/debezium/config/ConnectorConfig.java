/*
 * Copyright 2020 Tugdual Grall
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package io.redis.demos.debezium.config;

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

        props.setProperty("name", "engine");
        props.setProperty("connector.class",  "io.debezium.connector.mysql.MySqlConnector");

        props.setProperty("database.hostname", databaseHostname);
        props.setProperty("database.name", databaseName);
        props.setProperty("database.port", databasePort);
        props.setProperty("database.user", databaseUser);
        props.setProperty("database.password", databasePassword);
        props.setProperty("database.server.id", databaseServerId);
        props.setProperty("database.server.name", databaseServerName);
        props.setProperty("database.history", "io.debezium.relational.history.FileDatabaseHistory");
        props.setProperty("database.history.file.filename", "./dbhistory.dat");
        props.setProperty("table.whitelist", tableWhitelist);

        props.setProperty("offset.storage", "org.apache.kafka.connect.storage.FileOffsetBackingStore");
        props.setProperty("offset.storage.file.filename", "./offsets.dat");
        props.setProperty("offset.flush.interval.ms", "5000");

        return io.debezium.config.Configuration.from(props);
    }



}
