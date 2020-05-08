package io.redis.demos.debezium.sql.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@CrossOrigin(origins = "*")
@RequestMapping("/api/1.0/sql-rest-api/movies")
@RestController
public class MoviesAPIController {

    @Autowired
    JdbcTemplate jdbcTemplate;


    @GetMapping
    public List<Map<String,Object>> getMovies(
            @RequestParam(name="title", defaultValue="1") String orderBy,
            @RequestParam(name="page", defaultValue="1") int page) {
        List<Map<String,Object>> movies = new ArrayList<>();
        int offset = (page - 1) * 50;
        movies = jdbcTemplate.queryForList("SELECT * FROM movies ORDER BY ? LIMIT 50 OFFSET ?",  orderBy, offset );
        return  movies;
    }


    @GetMapping("/{id}")
    public Map<String,Object> findById(@PathVariable Long id) {
        Map<String,Object> movie = new HashMap<>();
        movie = jdbcTemplate.queryForMap("SELECT * FROM movies where movie_id = ?", id );
        return  movie;
    }

    @GetMapping("/{id}/actors")
    public List<Map<String,Object>> getMovieActors(@PathVariable Long id) {
        log.info("getMovieActors - {}", id);
        List<Map<String,Object>> actors = new ArrayList<>();
        String query =  "SELECT actors.* FROM actors, movies_actors "+
                        "WHERE movies_actors.movie_id = ? " +
                        "AND  movies_actors.actor_id = actors.actor_id";
        actors = jdbcTemplate.queryForList(query, id );
        return  actors;
    }


    @PostMapping("/")
    public Map<String,Object> save(@RequestBody Map<String,Object> record) {
        log.info(record.toString());
        Map<String,Object> actor = new HashMap<>();
        int rows = jdbcTemplate.update(
                "UPDATE movies SET title = ?, genre = ?, votes = ?, rating = ?, release_year = ? , plot = ? WHERE movie_id = ?",
                record.get("title"),
                record.get("genre"),
                record.get("votes"),
                record.get("rating"),
                record.get("release_year"),
                record.get("plot"),
                record.get("movie_id")
                );

        log.info("Movie Updated {} - {} row", record, rows);
        return record;
    }

}
