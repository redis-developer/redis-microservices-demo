
## Extending your application (comments)

Support you do not have access to the RDMS Schema (not allowed to create new tables), but you still want to add features to the application, for example:

* adding comments and rating to movie catalog

Using Redis you and a new micro service you can do it easily. (see `comments-service`)

### Script


1. Go on a movie
2. Show that 'Comments' tab at the bottom of the page
3. Enter a small comment
4. Save the comment
5. Add another one
6. See that the comments are sorted by creation date descending
7. Go to RedisInsight

The comment service use a RediSearch index to query the data an retrieve the comment by movie and date.

---
Next: [CDC with Debezium and Redis Streams](05-cdc-with-debezium-and-streams.md)