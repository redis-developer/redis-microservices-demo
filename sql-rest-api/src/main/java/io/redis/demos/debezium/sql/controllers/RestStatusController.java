package io.redis.demos.debezium.sql.controllers;

import io.redis.demos.debezium.sql.services.DataGeneratorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@CrossOrigin(origins = "*")
@RequestMapping("/api/1.0/sql-rest-api/")
@RestController
public class RestStatusController {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Inject
    DataGeneratorService dataGeneratorService;

    ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    @GetMapping("/status")
    public Map<String,String> status() {

        Map<String,String> result = new HashMap<>();

        result.put("service", "DataOperationGeneratorApplication");
        result.put("status", dataGeneratorService.getState());
        result.put("version", "1.0");

        return result;
    }

    @GetMapping("/start")
    public Map<String,String> start() {
        dataGeneratorService.start();
        Map<String,String> result = new HashMap<>();
        result.put("service", "DataOperationGeneratorApplication");
        result.put("operation", "start");
        return result;
    }

    @GetMapping("/stop")
    public Map<String,String> stop() {
        dataGeneratorService.stop();
        Map<String,String> result = new HashMap<>();
        result.put("service", "DataOperationGeneratorApplication");
        result.put("operation", "start");
        return result;
    }


}
