package io.redis.demos.debezium;

import io.redis.demos.debezium.listener.CDCEventListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/api/1.0/cache-invalidator/")
@RestController
@CrossOrigin("*")
@Slf4j
@org.springframework.context.annotation.Configuration
public class RestStatusController {

    @Inject
    CDCEventListener cdcEventListener;

    @CrossOrigin(origins = "*")
    @GetMapping("/status")
    public Map<String,String> status() {
        Map<String,String> result = new HashMap<>();
        result.put("service", "CacheInvalidatorServiceApplication");
        result.put("status", cdcEventListener.getState());
        result.put("version", "1.0");
        return result;
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/start")
    public Map<String,String> start() throws IOException {
        Map<String,String> result = new HashMap<>();
        cdcEventListener.startDebezium();
        result.put("service", "CacheInvalidatorServiceApplication");
        result.put("action", "start");
        return result;
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/stop")
    public Map<String,String> stop() throws IOException {
        Map<String,String> result = new HashMap<>();
        cdcEventListener.stopDebezium();
        result.put("service", "CacheInvalidatorServiceApplication");
        result.put("action", "stop");
        return result;
    }

}
