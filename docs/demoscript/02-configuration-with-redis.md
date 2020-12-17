
## Configuration with Redis

Redis can be used to share some configuration between services/application.

In this demonstration the service `caching-service` is used to store:

* the OMBD API Key
* a counter to increment the number of call to the API 


### Script


1. Open RedisInsight
2. Database is empty
3. Go in the "Services" page in the RMDB application
4. Save your OMDB API key in the form  (eg: `72a28845`)
5. Go back to RedisInsight, and list the keys

As you can see the `ms:config` hash is created.

You can imagine adding new fields to this hash for more application configuration options, for example `ENV:DEMO`.

Let's now use the the OMDB API key to call the Web service and cache the result..


---
Next: [Caching Web Services Calls](03-caching-web-service-calls,md)


