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

    <b-row class="mt-2"> 
      <b-col></b-col>
      <b-col>
      <b-card v-if="movieJson">
        <b-card-title>{{movieJson.title}}</b-card-title>
        <b-card-sub-title class="mb-2">{{movieJson.release_year}} </b-card-sub-title>
        <b-card-text>
          {{ movieJson.plot }}
        </b-card-text>
        <b-card-text>
          <img width=130 :src="movieJson.poster" />
        </b-card-text>
        <template v-slot:footer >
          <b-row class="small">
          <b-col class="text-left">
          {{ movieJson.genre }}
          </b-col>
          <b-col>
            <b-button size="sm" @click="goToMovie( movieJson.movie_id)">View</b-button>
          </b-col>
          <b-col class="text-right">
          {{ Number.parseFloat(movieJson.rating).toFixed(1) }}
          </b-col>
          </b-row>
        </template>
      </b-card>
      </b-col>
      <b-col></b-col>
    </b-row>
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

    <b-row class="mt-2"> 
      <b-col></b-col>
      <b-col>
        <b-card v-if="actorJson">
          <b-card-title>{{actorJson.first_name}} {{actorJson.last_name}}</b-card-title>
          <b-card-sub-title class="mb-2">{{actorJson.dob}} </b-card-sub-title>
          <template v-slot:footer>
            <b-button @click="goToActor( actorJson.actor_id)">
              View
            </b-button>
          </template>
        </b-card>
      </b-col>
      <b-col></b-col>
    </b-row>


    </b-jumbotron>
  </b-container >


  </div>
</template>

<script>

import { RepositoryFactory } from './../repositories/RepositoryFactory'
const RediSearchRepository = RepositoryFactory.get('dataStreamsRedisHashSyncRepository');

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
      movieJson : null,
      movies: [],
      movieSearch: '',
      selectedMovie: null,
      actorJson : null,
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
      this.movieJson = null;
      if (query != "") {
        const res = await fetch(API_URL.replace(':itemType', "movies").replace(':query', query));
        const suggestions = await res.json();
        this.selectedMovie = null;
        this.movies = suggestions;
        // whena single movie is selected
        if (this.movies && this.movies.length == 1  ) {
          this.selectedMovie = this.movies[0];
          const {data} = await RediSearchRepository.getMovieById(this.selectedMovie.id);
          this.movieJson = data;
        }


      } else {
        this.selectedMovie = null;
        this.movies = [];
      }
    },
    async getActors(query) {
      this.actorJson = null;
      if (query != "") {
        const res = await fetch(API_URL.replace(':itemType', "actors").replace(':query', query));
        const suggestions = await res.json();
        this.actors = suggestions;
        // whena single movie is selected
        if (this.actors && this.actors.length == 1  ) {
          this.selectedActor = this.actors[0];
          const {data} = await RediSearchRepository.getActorById(this.selectedActor.id);
          this.actorJson = data;
        }
      } else {
        this.selectedActor = null;
        this.actors = [];
      }
    },

    goToMovie(id) {
      this.$router.push({ name: 'MovieForm', params: { id: id }});
    },

    goToActor(id) {
      this.$router.push({ name: 'ActorForm', params: { id: id }});
    }

  },

  watch: {
    movieSearch: _.debounce(function(title) { this.getMovies(title) }, 500),
    actorSearch: _.debounce(function(name) { this.getActors(name) }, 500)
  }




}
</script>
