package io.redis.demos.debezium.graph;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@RestController
public class RestStatusController {

    @Inject
    RedisStreamToGraphService redisService;



    @GetMapping("/status")
    public Map<String,String> status() {

        Map<String,String> result = new HashMap<>();

        result.put("service", "SyncGraphApplication");
        result.put("status", "UP");
        result.put("version", "1.0");

        return result;
    }


    @GetMapping("/start")
    public Map<String,String> start() {
        Map<String,String> result = new HashMap<>();
        result.put("service", "SyncGraphApplication");
        result.put("action", "start");
        result.put("status", "OK");
        Map<String,String> call = redisService.processStream();
        result.putAll(call);
        return result;
    }

    @GetMapping("/relationship/{type}/{id}")
    public Map<String,String> findRelationshipsFromRDBMS(@PathVariable String type, @PathVariable int id) {
        Map<String,String> result = new HashMap<>();
        result.put("service", "SyncGraphApplication");
        result.put("action", "Refresh Relationships");
        result.put("status", "OK");
        redisService.createRelationFromMySQL(type, id);
        return result;
    }


}
