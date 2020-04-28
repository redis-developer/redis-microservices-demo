<template>
  <div id="formContainer" >


    <h1>{{ movie.title }} </h1>
    <hr/>

        <b-alert 
          v-if="msg" 
          variant="success" 
          dismissible
          show
          >
          {{msg}}
      </b-alert>

        <b-form @submit="onSubmit" @reset="onReset" v-if="show">
    <b-container >
    <b-row>
      <b-col>


          <b-form-group id="input-group-3" label="Title:" label-for="input-3">
            <b-form-input
              id="input-3"
              v-model="movie.title"
              required
            ></b-form-input>
          </b-form-group>


          <b-form-group id="input-group-3" label="Genre:" label-for="input-3">
            <b-form-input
              id="input-3"
              v-model="movie.genre"
              required
            ></b-form-input>
          </b-form-group>

          <b-form-group id="input-group-3" label="Votes:" label-for="input-3">
            <b-form-input
              id="input-3"
              v-model="movie.votes"
              required
            ></b-form-input>
          </b-form-group>

          <b-form-group id="input-group-3" label="Rating:" label-for="input-3">
            <b-form-input
              id="input-3"
              v-model="movie.rating"
              required
            ></b-form-input>
          </b-form-group>

          <b-form-group id="input-group-3" label="Release Year:" label-for="input-3">
            <b-form-input
              id="input-3"
              v-model="movie.release_year"
              required
            ></b-form-input>
          </b-form-group>

          <b-button type="submit" variant="primary">Submit</b-button>
          &nbsp;
          <b-button type="reset" variant="">Reset</b-button>

          <hr/>

          <div v-if="ratings" >
          <b-icon-reply class="large" @click="getRatings" />  
            -  IMDB : <span v-html='ratings["Internet Movie Database"]' /> 
            - Rotten Tomatoes : <span v-html='ratings["Rotten Tomatoes"]' />
            - Metacritic : <span v-html='ratings["Metacritic"]' />
          <template>
            <div>
            <b-form-checkbox v-model="callWithCache" name="check-button" switch>  
              Redis Cache  ( {{ratings.elapsedTimeMs}} ms)
            </b-form-checkbox>
            </div>
          </template>
          </div>

    </b-col>
    <b-col>
      <div>
      <b-img :src="movie.poster" />
      </div>
      
      <div>
        <b-form-textarea
        id="textarea"
        v-model="movie.plot"
        placeholder="Enter something..."
        rows="3"
        max-rows="6">
        </b-form-textarea>
      </div>

    </b-col>
    </b-row>
    </b-container>
    </b-form>


    <div id="listContainer">
      <b-table 
        striped
        hover
        v-if="show"
        :items="computedRecords"
      >
      </b-table>
    </div>



  </div>
</template>

<script>
import { RepositoryFactory } from './../repositories/RepositoryFactory'
const RDBMSRepository = RepositoryFactory.get('rdbmsRepository')
const CacheInvalidatorRepository = RepositoryFactory.get('cacheInvalidatorRepository')

export default {
  name: "MovieForm",
  components: {
  },
  data() {
    return {
      id: this.$route.params.id,
      movie : {
        title : null,
        genre : null,
        votes : null,
        rating : null,
        release_year : null,
        callWithCache : false,
      },
      ratings : {},
      actors: null,
      show: true,
      msg: null,
   };
  },
  created () {
    this.fetch();
    this.$parent.contextualHelp = "Legacy Application calling a REST API providing SQL data, "+
        "When a movie is updated, and the Streams service is running the database event will be pushed to Redis Streams, and then can be consumed by any type of application.<br/> "+
        "See  <b><a target='_blank' href='https://github.com/tgrall/redis-microservices-demo/blob/master/sql-data-generator/src/main/java/io/redis/demos/debezium/sql/controllers/MoviesAPIController.java'>MoviesAPIController</a></b>";
  },
  methods: {
    async fetch () {
      this.isLoading = true
      let { data } = await RDBMSRepository.findMovieById( this.$route.params.id)
      this.movie = data;
      data = await RDBMSRepository.findMovieActors( this.$route.params.id)
      this.actors = data.data;
      this.getRatings();
      this.isLoading = false;
    },
    async onSubmit(evt) {
      evt.preventDefault()
      await RDBMSRepository.updateMovie( this.movie)
      this.msg = "Movie Updated";
    },
    onReset(evt) {
      evt.preventDefault()
    },
   async getRatings() {
      // Get ratings from WS call
      if (this.movie.imdb_id) {
        this.ratings = undefined;
        const {data} = await CacheInvalidatorRepository.getRatings(this.movie.imdb_id, this.callWithCache);
        this.ratings = data;
      }
    }
  },
  computed: {
    computedRecords () {
      return this.actors; 
    }
  }
};
</script>

<style scoped>
#formContainer {
  padding-right: 100px;
  padding-left: 100px;

}

#listContainer {
  padding-top: 20px;
}


</style>