package io.redis.demos.debezium.graph;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class RestStatusController {

    @GetMapping("/status")
    public Map<String,String> status() {

        Map<String,String> result = new HashMap<>();

        result.put("service", "SyncGraphApplication");
        result.put("status", "UP");
        result.put("version", "1.0");

        return result;
    }

}
