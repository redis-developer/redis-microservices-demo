package io.redis.demos.debezium;

import io.redis.demos.debezium.listener.CDCEventListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@org.springframework.context.annotation.Configuration
public class RestStatusController {

    @Inject
    CDCEventListener cdcEventListener;

    @GetMapping("/status")
    public Map<String,String> status() {

        Map<String,String> result = new HashMap<>();

        result.put("service", "CacheInvalidatorServiceApplication");
        result.put("status", "UP");
        result.put("version", "1.0");

        return result;
    }

    @GetMapping("/restart")
    public Map<String,String> start() throws IOException {
        Map<String,String> result = new HashMap<>();

        cdcEventListener.startDebezium();
        result.put("service", "CacheInvalidatorServiceApplication.restart");
        result.put("action", "OK");

        return result;
    }
}
