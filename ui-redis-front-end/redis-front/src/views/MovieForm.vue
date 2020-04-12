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
      },
      actors: null,
      show: true,
      msg: null,
   };
  },
  created () {
    this.fetch()
  },
  methods: {
    async fetch () {
      this.isLoading = true
      let { data } = await RDBMSRepository.findMovieById( this.$route.params.id)
      this.movie = data;
      data = await RDBMSRepository.findMovieActors( this.$route.params.id)
      this.actors = data.data;
      this.isLoading = false
    },
    async onSubmit(evt) {
      evt.preventDefault()
      await RDBMSRepository.updateMovie( this.movie)
      this.msg = "Movie Updated";
    },
    onReset(evt) {
      evt.preventDefault()
    },
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