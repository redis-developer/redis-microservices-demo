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

    public static void main(String[] args) {


        Jedis jedis = new Jedis();
        Client client = new Client("demo:search", "localhost", 6379);
        client.dropIndex();
        client.createIndex(MoviesSchema.getSchema(), Client.IndexOptions.defaultOptions());


        try {
            client.getInfo();
            client.dropIndex();
            client.createIndex(MoviesSchema.getSchema(), Client.IndexOptions.defaultOptions());
            System.out.println("Index exists");
        } catch(JedisDataException jde) {
            if (jde.getMessage().equalsIgnoreCase("Unknown Index name")) {
                client.createIndex(MoviesSchema.getSchema(), Client.IndexOptions.defaultOptions());
            }
        }

        String k1 = "aa:01";
        if ( !jedis.exists(k1) ) {

            Map<String, Object> fields = new HashMap<>();
            fields.put(TITLE, "Guardians of the Galaxy");
            fields.put(GENRE, "Action");
            fields.put(VOTES, 704613);
            fields.put(RATING, 8.2);
            fields.put(RELEASE_YEAR, 2014);
            fields.put(PLOT, null);
            fields.put( POSTER, null);

            //fields.values().removeIf(Objects::isNull);

            try {

                System.out.println(fields);

                client.addDocument(k1, fields);


                Thread.sleep(3000);


            } catch (Exception e) {
                e.printStackTrace();
            }


        }


    }


}
