package io.redis.demos.debezium;

import io.redis.demos.debezium.listener.CDCEventListener;
import io.redis.demos.debezium.service.WebServiceCachingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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

    @Inject WebServiceCachingService webServiceCachingService;

    @GetMapping("/status")
    public Map<String,String> status() {
        Map<String,String> result = new HashMap<>();
        result.put("service", "CacheInvalidatorServiceApplication");
        result.put("status", cdcEventListener.getState());
        result.put("version", "1.0");
        return result;
    }

    @GetMapping("/start")
    public Map<String,String> start() throws IOException {
        Map<String,String> result = new HashMap<>();
        cdcEventListener.startDebezium();
        result.put("service", "CacheInvalidatorServiceApplication");
        result.put("action", "start");
        return result;
    }

    @GetMapping("/stop")
    public Map<String,String> stop() throws IOException {
        Map<String,String> result = new HashMap<>();
        cdcEventListener.stopDebezium();
        result.put("service", "CacheInvalidatorServiceApplication");
        result.put("action", "stop");
        return result;
    }


    @GetMapping("/ratings/{id}")
    public Map<String,Object> getRatings(
            @PathVariable(name = "id") String imdbId,
            @RequestParam(name="cache", defaultValue = "1") String withCache) throws IOException {
        Map<String,Object> result = new HashMap<>();

        Map<String,String> resultWsCall = webServiceCachingService.getRatings(imdbId, withCache.equals("1"));
        result.putAll(resultWsCall);

        return result;
    }


}
