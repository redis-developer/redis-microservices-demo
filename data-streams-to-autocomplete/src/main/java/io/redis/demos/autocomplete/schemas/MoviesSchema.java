package io.redis.demos.autocomplete.schemas;

import io.redisearch.Schema;
import io.redisearch.client.Client;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisDataException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MoviesSchema {

    public final static String MOVIE_ID = "movie_id";
    public final static String TITLE = "title";
    public final static String GENRE = "genre";
    public final static String VOTES = "votes";
    public final static String RATING = "rating";
    public final static String RELEASE_YEAR = "release_year";
    public final static String PLOT = "plot";
    public final static String POSTER = "poster";

    public static Schema getSchema() {
        return new Schema()
                .addTextField(TITLE, 5.0)
                .addTextField(PLOT, 1.0)
                .addNumericField(RELEASE_YEAR)
                .addNumericField(RATING)
                .addNumericField(VOTES);
    }

}
