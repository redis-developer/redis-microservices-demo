gb = GearsBuilder('StreamReader') 
gb.foreach(lambda x: execute('PUBLISH', 'ms:notifications', '{ "id":"'+ x['movie_id'] +'", "title":"'+   x['title'] +'", "type":"movie"}'))  # write to Redis Hash
gb.register("events:inventory:movies")
