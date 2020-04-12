package io.redis.demos.autocomplete;

import io.redisearch.SearchResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.*;

@Slf4j
@CrossOrigin(origins = "*")
@RequestMapping("/api/1.0/data-streams-to-autocomplete/")
@RestController
public class RestStatusController {

    @Inject
    RediStreamsToAutocomplete redisService;

    @GetMapping("/{object}/autocomplete")
    public List<Map<String,Object>> autoComplete(@PathVariable(name = "object")String object,   @RequestParam(name="q")String term) {
        return redisService.suggest(object, term);
    }

    @GetMapping("/search/{object}")
    public Map<String,Object> search(@PathVariable(name = "object")String object,   @RequestParam(name="q")String term) {
        return redisService.search(object, term);
    }


    @GetMapping("/status")
    public Map<String,String> status() {
        Map<String,String> result = new HashMap<>();
        result.put("service", "AutocompleteSyncApplication");
        result.put("status", redisService.getState());
        result.put("suggest", String.valueOf(redisService.isSuggest()));
        result.put("fulltext", String.valueOf(redisService.isFulltext()));
        result.put("version", "1.0");
        return result;
    }

    @GetMapping("/start")
    public Map<String,String> start() {
        Map<String,String> result = new HashMap<>();
        result.put("service", "AutocompleteSyncApplication");
        result.put("action", "start");
        Map<String,String> call = redisService.processStream();
        result.putAll(call);
        return result;
    }

    @GetMapping("/stop")
    public Map<String,String> stop() {
        Map<String,String> result = new HashMap<>();
        result.put("service", "AutocompleteSyncApplication");
        result.put("action", "stop");
        Map<String,String> call = redisService.stopProcessStream();
        result.putAll(call);
        return result;
    }

    @GetMapping("/config/{object}/index-info")
    public Map<String,Object> getInfoIndex(@PathVariable(name = "object")String object) {
        return redisService.getInfoIndex(object);
    }

    @GetMapping("/config/fulltext")
    public Map<String,String> configureFullText() {
        Map<String,String> result = new HashMap<>();
        result.put("service", "AutocompleteSyncApplication");
        result.put("action", "configureFullText");
        Map<String,String> call = redisService.changeFullText();
        result.putAll(call);
        return result;
    }

    @GetMapping("/config/autocomplete")
    public Map<String,String> configureAutocomplete() {
        Map<String,String> result = new HashMap<>();
        result.put("service", "AutocompleteSyncApplication");
        result.put("action", "configureFullText");
        Map<String,String> call = redisService.changeSuggest();
        result.putAll(call);
        return result;
    }


}
