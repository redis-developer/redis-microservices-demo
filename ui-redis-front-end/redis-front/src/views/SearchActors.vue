<template>
  <div id="app" class="px-8 py-8 min-h-screen">

    <h1> <b-icon-people-circle />  Search Actors with RediSearch</h1>
    
    <b-form @submit="onSubmit" @reset="onReset" >
      <b-input-group
        >
        <b-form-input
            v-model="searchQuery"
        >
        </b-form-input>
        <b-input-group-append>
          <b-button size="sm" text="Button" @click="search"  >Search</b-button>
        </b-input-group-append>
      </b-input-group>
    </b-form>


<hr>

<b-row>
  <div 
    v-for="doc in searchResult.docs"
    :key="doc.movie_id"
    class="col-md-3 col-4 my-1">

    <b-card>
      <b-card-title>{{doc.body.first_name}} {{doc.body.last_name}}</b-card-title>
       <b-card-sub-title class="mb-2">{{doc.body.dob}} </b-card-sub-title>

      <template v-slot:footer>
        
        <b-button @click="goToForm( doc.body.actor_id)">
          View
        </b-button>
      </template>

    </b-card>


  </div>
</b-row>
  

  </div>
</template>

<script>
import { RepositoryFactory } from './../repositories/RepositoryFactory'
const RediSearchRepository = RepositoryFactory.get('dataStreamsAutoCompleteRepository');


export default {
  name: "Search",
  data() {
    return {
      searchQuery : "",
      searchResult : {},
    }
  },
  created() {
        this.$parent.contextualHelp = 
        "This search form is based on RediSearch on the Movie index (<i>\"idx:ms:search:index:movies\"</<i>).<br>You can use various query such as:"+
        "<ul><li>wars -CIVIL</li></ul>"+
        "<p>Update, create a movie and you will see that the index is updated. (If the <a href='/services'>service</a> is running)</p>";

  },
  methods : {
    onSubmit(evt) {
      evt.preventDefault()
      this.search();
    },
    onReset(evt) {
        evt.preventDefault()

    },
    async search() {
      console.log( this.searchQuery );

      const {data} = await RediSearchRepository.search("actors", this.searchQuery);
      console.log(data);
      this.searchResult = data;

    },
    goToForm(id) {
      console.log(id);
      this.$router.push({ name: 'ActorForm', params: { id: id }});
    }
  }
};
</script>
