const express = require('express');
const cors = require('cors');
const app = express();
const nconf = require('nconf');

nconf.argv();
nconf.env();

nconf.file({ file: (nconf.get('CONF_FILE') || './config.dev.json' ) });
console.log(`... starting node host :\n\t port: ${nconf.get("PORT")}`);

const serverPort = nconf.get('PORT') || 8086;


const CommentsService = require('./commentsService');
const searchService = new CommentsService();

app.use(cors());
app.use(express.json())

app.get('/api/1.0/comments/:id', (req, res) => {
    const id = req.params.id;
    searchService.getComment(id, function (err, result) {
        res.json(result);
    });
});

app.delete('/api/1.0/comments/:id', (req, res) => {
    const id = req.params.id;
    searchService.deleteComment(id, function (err, result) {
        res.json(result);
    });
});

app.post('/api/1.0/comments/movie/:id', (req, res) => {
     searchService.addMovieComment(req.body, function (err, result) {
         res.json(result);
     });
});

app.get('/api/1.0/comments/movie/:id', (req, res) => {
    const id = req.params.id;
    const offset = Number((req.query.offset) ? req.query.offset : '0');
    const limit = Number((req.query.limit) ? req.query.limit : '10');
    const sortBy = req.query.sortby;
    const ascending = req.query.ascending;

    const options = {
        offset,
        limit
    };

    if (sortBy) {
        options.sortBy = sortBy;
        options.ascending = true; // if sorted by default it is ascending
    } else {
        options.sortBy = "timestamp";
        options.ascending = false; 
    }

    if (ascending) {
        options.ascending = (ascending == 1 || ascending.toLocaleLowerCase() === 'true');
    }

    // Retrive the services
    searchService.getMovieComments(id, options, function (err, result) {
        res.json(result);
    });
});


app.get('/api/1.0/', (req, res) => {
    res.json({ status: 'started' });
});


app.get('/', (req, res) => {
    res.send('Comment Service Node Node REST Server Started');
});

app.listen(serverPort, () => {
    console.log(`Comment Service Node listening at http://localhost:`+ nconf.get('PORT'));
});

