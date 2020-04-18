const WebSocket = require('ws');
    nconf = require('nconf'),
    redis = require('redis');

nconf.argv();
nconf.env();

console.log('----> '+ nconf.get('CONF_FILE'));


nconf.file({ file: (nconf.get('CONF_FILE') || './config.dev.json' ) });
console.log(`... starting node host :\n\t port: ${nconf.get("PORT")} \n\t Redis : ${nconf.get("REDIS_HOST")}:${nconf.get("REDIS_PORT")}`);

var db = redis.createClient(nconf.get("REDIS_PORT"), nconf.get("REDIS_HOST"));
if (nconf.get('REDIS_PASSWORD')) {
	db.auth(nconf.get("REDIS_PASSWORD"));
}
db.subscribe('ms:notifications');

const wss = new WebSocket.Server({ port: nconf.get('PORT') });

wss.on('connection', function connection(ws) {

  ws.on('message', function incoming(message) {
    console.log('received: %s', message);
  });

  db.on('message', function(channel, message) {
    ws.send(message);
  });

});


console.log('listening at ws://localhost:' + nconf.get('PORT'));