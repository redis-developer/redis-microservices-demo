# Redis Streams to RediSearch

This service:

* Reads various Redis Streams and update RediSearch index and/or Suggestion key
* Exposes the search as REST API Endpoint


## Sample Queries:

Once you have consumes the data into the index and suggestion you can do the following commands in Redis Insight or CLI.

Keys for Suggestion:

* `"ms:search:suggest:movies"`
* `"ms:search:suggest:actors"`

Full text indices:

* `"ms:search:index:movies"`
* `"ms:search:index:actors"`

### Sample Autocomplete queries:

```
> FT.SUGGET "ms:search:suggest:movies" jo

> FT.SUGGET "ms:search:suggest:movies" jo FUZZY

> FT.SUGGET "ms:search:suggest:actors" jo 

> FT.SUGGET "ms:search:suggest:actors" jo FUZZY

```

You can test the same queries using the UI, or a REST call:

```
$ curl hhttp://localhost:8085/api/1.0/data-streams-to-autocomplete/actors/autocomplete?q=Jo

$ http://localhost:8085/api/1.0/data-streams-to-autocomplete/movies/autocomplete?q=Jo
```

### Full Text Query

```

>  FT.SEARCH "ms:search:index:movies" "star" LIMIT 0 10


>  FT.SEARCH "ms:search:index:actors" "john" LIMIT 0 10


```

**Aggregations**

```
>  FT.AGGREGATE "ms:search:index:movies" "*" GROUPBY 1 @release_year REDUCE COUNT 0 AS releases

```