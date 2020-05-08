package io.redis.demos.services.caching;

import io.redis.demos.services.caching.listener.CDCEventListener;
import io.redis.demos.services.caching.service.WebServiceCachingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/api/1.0/caching/")
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
        result.put("service", "CachingServiceApplication");
        result.put("status", cdcEventListener.getState());
        result.put("version", "1.0");
        return result;
    }

    @GetMapping("/start")
    public Map<String,String> start() throws IOException {
        Map<String,String> result = new HashMap<>();
        cdcEventListener.startDebezium();
        result.put("service", "CachingServiceApplication");
        result.put("action", "start");
        return result;
    }

    @GetMapping("/stop")
    public Map<String,String> stop() throws IOException {
        Map<String,String> result = new HashMap<>();
        cdcEventListener.stopDebezium();
        result.put("service", "CachingServiceApplication");
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


    @PostMapping("/configuration/omdb_api")
    public Map<String,String> saveOmdbApiKey(@RequestParam(name="key") String key) throws IOException {
        Map<String,String> result = new HashMap<>();
        webServiceCachingService.saveOMDBAPIKey(key);
        result.put("status", "OK");
        result.put("msg", "OMDB API Key successfully saved");
        return result;
    }

    @GetMapping("/configuration/omdb_api")
    public Map<String,String> getOmdbApiKey() throws IOException {
        Map<String,String> result = new HashMap<>();
        result.put("key", webServiceCachingService.OMDB_API_KEY);
        result.put("value", webServiceCachingService.getOMDBAPIKey());
        return result;
    }
}
