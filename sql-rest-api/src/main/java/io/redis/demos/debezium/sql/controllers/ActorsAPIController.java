package io.redis.demos.debezium.sql.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@CrossOrigin(origins = "*")
@RequestMapping("/api/1.0/sql-rest-api/actors")
@RestController
public class ActorsAPIController {

    @Autowired
    JdbcTemplate jdbcTemplate;


    @GetMapping
    public List<Map<String,Object>> getActors(
            @RequestParam(name="last_name", defaultValue="1") String orderBy,
            @RequestParam(name="page", defaultValue="1") int page) {
        List<Map<String,Object>> actors = new ArrayList<>();
        int offset = (page - 1) * 50;
        actors = jdbcTemplate.queryForList("SELECT * FROM actors ORDER BY ? LIMIT 50 OFFSET ?",  orderBy, offset );
        return  actors;
    }

    @GetMapping("/{id}")
    public Map<String,Object> findById(@PathVariable Long id) {
        Map<String,Object> actor = new HashMap<>();
        actor = jdbcTemplate.queryForMap("SELECT * FROM actors where actor_id = ?", id );
        return  actor;
    }

    @PostMapping("/")
    public Map<String,Object> save(@RequestBody Map<String,Object> record) {
        Map<String,Object> actor = new HashMap<>();
        int rows = jdbcTemplate.update(
                "UPDATE actors SET first_name = ?, last_name = ?, dob = ? WHERE actor_id = ?",
                record.get("first_name"),
                record.get("last_name"),
                record.get("dob"),
                record.get("actor_id")
                );

        log.info("Actor Updated {} - {} row", record, rows);
        return record;
    }

}
