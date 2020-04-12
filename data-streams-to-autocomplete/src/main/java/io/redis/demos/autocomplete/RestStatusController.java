package io.redis.demos.autocomplete;

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

    @GetMapping("/status")
    public Map<String,String> status() {
        Map<String,String> result = new HashMap<>();
        result.put("service", "AutocompleteSyncApplication");
        result.put("status", redisService.getState());
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



    @GetMapping("/{object}/autocomplete")
    public List<Map<String,Object>> autoComplete(@PathVariable(name = "object")String object,   @RequestParam(name="q")String term) {
          return redisService.suggest(object, term);
    }



}
