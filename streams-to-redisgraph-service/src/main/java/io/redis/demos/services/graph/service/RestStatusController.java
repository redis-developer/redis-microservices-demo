package io.redis.demos.services.graph.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@CrossOrigin(origins = "*")
@RequestMapping("/api/1.0/graph-service/")
@RestController
public class RestStatusController {

    @Inject
    RedisStreamToGraphService redisService;

    @GetMapping("/status")
    public Map<String,String> status() {

        Map<String,String> result = new HashMap<>();

        result.put("service", "GraphServiceApplication");
        result.put("status", redisService.getState());
        result.put("version", "1.0");

        return result;
    }

    @GetMapping("/start")
    public Map<String,String> start() {
        Map<String,String> result = new HashMap<>();
        result.put("service", "GraphServiceApplication");
        result.put("action", "start");
        Map<String,String> call = redisService.processStream();
        result.putAll(call);
        return result;
    }

    @GetMapping("/stop")
    public Map<String,String> stop() {
        Map<String,String> result = new HashMap<>();
        result.put("service", "GraphServiceApplication");
        result.put("action", "stop");
        Map<String,String> call = redisService.stopProcessStream();
        result.putAll(call);
        return result;
    }

    @GetMapping("/relationship/{type}/{id}")
    public Map<String,String> findRelationshipsFromRDBMS(@PathVariable String type, @PathVariable int id) {
        Map<String,String> result = new HashMap<>();
        result.put("service", "GraphServiceApplication");
        result.put("action", "Refresh Relationships");
        redisService.createRelationFromMySQL(type, id);
        return result;
    }

    @GetMapping("/executeQuery/{graphName}")
    public ResultSet executeQuery(@PathVariable(name = "graphName") String graphName, @RequestHeader(name="query") String query) {
        return redisService.executeQuery(graphName, query);
    }


}
