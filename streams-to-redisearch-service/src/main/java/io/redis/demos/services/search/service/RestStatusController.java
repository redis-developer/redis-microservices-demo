package io.redis.demos.services.search.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.*;

@Slf4j
@CrossOrigin(origins = "*")
@RequestMapping("/api/1.0/search-service/")
@RestController
public class RestStatusController {

    @Inject StreamsToRediSearch redisService;

    @GetMapping("/{object}/autocomplete")
    public List<Map<String,Object>> autoComplete(@PathVariable(name = "object")String object,   @RequestParam(name="q")String term) {
        return redisService.suggest(object, term);
    }

    @GetMapping("/search/{object}")
    public Map<String,Object> search(@PathVariable(name = "object")String object,   @RequestParam(name="q")String term) {
        return redisService.search(object, term);
    }

    @GetMapping("/search-with-pagination")
    public Map<String,Object> searchWithPagination(
            @RequestParam(name="q")String query,
            @RequestParam(name="offset", defaultValue="0")int offset,
            @RequestParam(name="limit", defaultValue="10")int limit,
            @RequestParam(name="sortby", defaultValue="")String sortBy,
            @RequestParam(name="ascending", defaultValue="true")boolean ascending) {
        return redisService.searchWithPagination(query, offset, limit, sortBy,ascending);
    }

    @GetMapping("/status")
    public Map<String,String> status() {
        Map<String,String> result = new HashMap<>();
        result.put("service", "SearchServiceApplication");
        result.put("status", redisService.getState());
        result.put("suggest", String.valueOf(redisService.isSuggest()));
        result.put("fulltext", String.valueOf(redisService.isFulltext()));
        result.put("version", "1.0");
        return result;
    }

    @GetMapping("/start")
    public Map<String,String> start() {
        Map<String,String> result = new HashMap<>();
        result.put("service", "SearchServiceApplication");
        result.put("action", "start");
        Map<String,String> call = redisService.processStream();
        result.putAll(call);
        return result;
    }

    @GetMapping("/stop")
    public Map<String,String> stop() {
        Map<String,String> result = new HashMap<>();
        result.put("service", "SearchServiceApplication");
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
        result.put("service", "SearchServiceApplication");
        result.put("action", "configureFullText");
        Map<String,String> call = redisService.changeFullText();
        result.putAll(call);
        return result;
    }

    @GetMapping("/config/autocomplete")
    public Map<String,String> configureAutocomplete() {
        Map<String,String> result = new HashMap<>();
        result.put("service", "SearchServiceApplication");
        result.put("action", "configureFullText");
        Map<String,String> call = redisService.changeSuggest();
        result.putAll(call);
        return result;
    }

    @GetMapping("/stats/movies/all")
    public Map<String,Object> allStats(@RequestParam(name="sort", defaultValue="year")String orderBy) {
        Map<String,Object> result = new HashMap<>();
        Map<String,Object> stats = redisService.getMovieByYear(orderBy, 100);
        result.put("movieByYear", stats);
        stats = redisService.getMovieByGenre("genre", 100);
        result.put("movieByGenre", stats);
        return result;
    }

    @GetMapping("/movies/genres")
    public Map<String,Object> getAllGenres() {
        Map<String,Object> result = new HashMap<>();
        result = redisService.getAllGenres();
        return result;
    }

    @GetMapping("/movies/group_by/{field}")
    public Map<String,Object> getMovieGroupBy(@PathVariable("field") String field) {
        return redisService.getMovieGroupBy(field);
    }

    @GetMapping("/movies/{id}")
    public Map<String,String> getMovieById(@PathVariable("id") String id) {
        return redisService.getMovieById(id);
    }

    @GetMapping("/actors/{id}")
    public Map<String,String> getActorById(@PathVariable("id") String id) {
        return redisService.getActorById(id);
    }
}
