package io.redis.demos.services.search.service.schemas;

import io.redisearch.Schema;

public class ActorsSchema {

    public final static String ACTOR_ID = "actor_id";
    public final static String FIRST_NAME = "first_name";
    public final static String LAST_NAME = "last_name";
    public final static String DOB = "dob";

    public static Schema getSchema() {
        return new Schema()
                .addTextField(FIRST_NAME, 5.0)
                .addTextField(LAST_NAME, 5.0);
    }

}
