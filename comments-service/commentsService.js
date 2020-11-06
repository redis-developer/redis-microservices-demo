
const redis = require('redis');
const redisearch = require('redis-redisearch');
const nconf = require('nconf');


const indexName = process.env.REDIS_INDEX || 'ms:search:index:comments:movies';
const commentsKeyPrefix = process.env.REDIS_COMMENT_PREFIX || 'ms:comments:';

nconf.argv();
nconf.env();

nconf.file({ file: (nconf.get('CONF_FILE') || './config.dev.json' ) });
console.log(`\t Redis : ${nconf.get("REDIS_HOST")}:${nconf.get("REDIS_PORT")}`);

const redisUrl = process.env.REDIS_URL || `redis://${nconf.get("REDIS_HOST")}:${nconf.get("REDIS_PORT")}`;


redisearch(redis);
const client = redis.createClient(redisUrl);


const CommentService = function () {

    const _getComment = function(id, callback) {
        // using hgetall, since the hash size is limited
        client.hgetall(id, function(err, res) {
            callback( err, res );
        });
    }

    const _deleteComment = function(id, callback) {
        // using hgetall, since the hash size is limited
        client.del(id, function(err, res) {
            callback( err, res );
        });
    }


    const _addMovieComment = function(comment, callback) {
        const ts = Date.now();
        const key = `${commentsKeyPrefix}movie:${comment.movie_id}:${ts}`

        comment.timestamp = ts;

        const values = [
            "movie_id" , comment.movie_id,
            "user_id" , comment.user_id,
            "comment" , comment.comment,
            "rating" , comment.rating,
            "timestamp" , comment.timestamp,
        ];
        client.hmset(key, values, function(err, res) {
            callback( err, { "id" : key, "comment" : comment  } );
        });
    }

    /**
     * Retrieve the list of comments for a movie
     * @param {*} id 
     * @param {*} options 
     * @param {*} callback 
     */
    const _getMovieComments = function(id, options, callback) {

        let offset = 0; // default values
        let limit = 10; // default value

        const queryString = `@movie_id:[${id} ${id}]`

        // prepare the "native" FT.SEARCH call
        // FT.SEARCH IDX_NAME queryString  [options]
        const searchParams = [
            indexName,    // name of the index
            queryString,  // query string
            'WITHSCORES'  // return the score
        ];

        // if limit add the parameters
        if (options.offset || options.limit) {
            offset = options.offset || 0;
            limit = options.limit || 10
            searchParams.push('LIMIT');
            searchParams.push(offset);
            searchParams.push(limit);
        }
        // if sortby add the parameters  
        if (options.sortBy) {
            searchParams.push('SORTBY');
            searchParams.push(options.sortBy);
            searchParams.push((options.ascending) ? 'ASC' : 'DESC');
        }

        client.ft_search(
            searchParams,
            function (err, searchResult) {

                const totalNumberOfDocs = searchResult[0];
                const result = {
                    meta: {
                        totalResults: totalNumberOfDocs,
                        offset,
                        limit,
                        queryString,
                    },
                    docs: [],
                }

                // create JSON document from n/v pairs
                for (let i = 1; i <= searchResult.length - 1; i++) {
                    const doc = {
                        meta: {
                            score: Number(searchResult[i + 1]),
                            id: searchResult[i]
                        }
                    };
                    i = i + 2;
                    doc.fields = {};
                    const fields = searchResult[i]
                    if (fields) {
                        for (let j = 0, len = fields.length; j < len; j++) {
                            const idxKey = j;
                            const idxValue = idxKey + 1;
                            j++;
                            doc.fields[fields[idxKey]] = fields[idxValue];

                            // To make it easier let's format the timestamp
                            if (fields[idxKey] == "timestamp") {
                                const date = new Date(parseInt(fields[idxValue]));
                                doc.fields["dateAsString"] =  date.toDateString()+" - "+date.toLocaleTimeString() ;
                            }
                        }
                    }
                    result.docs.push(doc);
                }

                callback(err, result);
            }
        );
    }

    return {
        getComment: _getComment,
        getMovieComments: _getMovieComments,
        addMovieComment: _addMovieComment,
        deleteComment: _deleteComment,
    };
}

module.exports = CommentService;

