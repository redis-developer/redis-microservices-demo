<template>
  <div class="autocomplete">

  <b-container >
    
    <b-jumbotron>

    <vue-bootstrap-typeahead
      :data="movies"
      prepend="Title:"
      v-model="movieSearch"
      size="lg"
      :serializer="s => s.string"
      placeholder="Type a movie title..."
      @hit="selectMovie = $event"
    >
    </vue-bootstrap-typeahead>
    </b-jumbotron>

    <b-jumbotron>
    <vue-bootstrap-typeahead
      :data="actors"
      prepend="Name:"
      v-model="actorSearch"
      size="lg"
      :serializer="s => s.string"
      placeholder="Type actor name.."
      @hit="selectActor = $event"
    >
    </vue-bootstrap-typeahead>
    </b-jumbotron>
  </b-container >


  </div>
</template>

<script>
// TODO : see how to show all result in the list (the component check the search string, not compatible with fuzzy search)

// @ is an alias to /src
import VueBootstrapTypeahead from 'vue-bootstrap-typeahead'
import _ from 'underscore'

const API_URL = '/api/fulltext/:itemType/autocomplete?q=:query'


export default {
  name: 'Autocomplete',
  components: {
    VueBootstrapTypeahead
  },
  data() {
    return {
      movies: [],
      movieSearch: '',
      selectedMovie: null,
      actors: [],
      actorSearch: '',
      selectedActor: null,
    }
  },
  created() {
    this.$parent.contextualHelp = 
     "This part of the application shows RediSearch Suggest feature. Actors & Movies data are pushed in real time from th RDBMS to Redis using Streams." + 
     "Then a consumer is reading the data and push the data into  "+
     "<b><a target='blank' href='https://github.com/tgrall/redis-microservices-demo/blob/master/streams-to-redisearch-service/src/main/java/io/redis/demos/services/search/service/StreamsToRediSearch.java'> Redis key</a></b>.<br/>" + 
     "The data are then accessed using a generic <b><a target='blank' href='https://github.com/tgrall/redis-microservices-demo/blob/master/streams-to-redisearch-service/src/main/java/io/redis/demos/services/search/service/RestStatusController.java'>REST API</a></b>.<br/><br/>" +
     "You can easily reuse the classes to add new Autocomplete feature to your application. (returning an id in the payload)" 
     ;
  },
  methods: {
    async getMovies(query) {
      if (query != "") {
        const res = await fetch(API_URL.replace(':itemType', "movies").replace(':query', query));
        const suggestions = await res.json();
        console.log(suggestions)
        this.movies = suggestions;
      } else {
        this.movies = [];
      }
    },
    async getActors(query) {
      if (query != "") {
        const res = await fetch(API_URL.replace(':itemType', "actors").replace(':query', query));
        const suggestions = await res.json();
        console.log(suggestions)
        this.actors = suggestions;
      } else {
        this.actors = [];
      }
    }
  },

  watch: {
    movieSearch: _.debounce(function(title) { this.getMovies(title) }, 500),
    actorSearch: _.debounce(function(name) { this.getActors(name) }, 500)
  }  
}
</script>
