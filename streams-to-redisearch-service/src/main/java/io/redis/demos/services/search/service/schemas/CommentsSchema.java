package io.redis.demos.services.search.service.schemas;

import io.redisearch.Schema;

public class CommentsSchema {

    public final static String MOVIE_ID = "movie_id";
    public final static String TIMESTAMP = "timestamp";
    public final static String USER_ID = "user_id";
    public final static String COMMENT = "comment";
    public final static String RATING = "rating";

    public static Schema getSchema() {
        return new Schema()
                .addNumericField(MOVIE_ID)
                .addSortableNumericField(TIMESTAMP)
                .addSortableTagField(USER_ID, ",")
                .addTextField(COMMENT, 1.0)
                .addSortableNumericField(RATING);
    }

}
