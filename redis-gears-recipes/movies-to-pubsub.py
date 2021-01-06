gb = GearsBuilder('StreamReader') 
gb.foreach(lambda x: execute('PUBLISH', 'ms:notifications', '{ "id":"'+ x["value"]['movie_id'] +'", "title":"'+   x["value"]['title'] +'", "type":"movie"}'))  
gb.register("events:inventory:movies")
