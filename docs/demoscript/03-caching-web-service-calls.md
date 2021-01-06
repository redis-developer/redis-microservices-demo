
## Caching Web Services Calls

In the demonstration, Redis is used to cache the result of OMDB API calls.

This Web service is used to get the ratings for the selected movie, and you can select to cache or not the result in Redis.


### Script

1. Go to the Movies list
2. Click on the first movie
3. The ratings are visible (under the submit button)
3. Look at the elapsed time and the number of calls
4. Click on the green button to call the service again (since it is not cache)
5. Enable caching for the web service using the sliding button
6. Click on the green button again, you will see that it is faster and the number of calls does not increase.
7. Go to RedisInsight an look at the keys, you will see a key for the movie rating
8. You can see the TTL of each key too.



---
Next: [Extending your application (comments)](04-extending-your-application-comments.md)