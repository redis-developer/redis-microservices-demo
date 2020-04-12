<template>
  <div id="formContainer" >

    <h1>{{ actor.first_name }} {{ actor.last_name }} </h1>
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


      <b-form-group id="input-group-3" label="First Name:" label-for="input-3">
        <b-form-input
          id="input-3"
          v-model="actor.first_name"
          required
        ></b-form-input>
      </b-form-group>

      <b-form-group id="input-group-3" label="Last Name:" label-for="input-3">
        <b-form-input
          id="input-3"
          v-model="actor.last_name"
          required
        ></b-form-input>
      </b-form-group>

      <b-form-group id="input-group-3" label="Date of Birth:" label-for="input-3">
        <b-form-input
          id="input-3"
          v-model="actor.dob"
          required
        ></b-form-input>
      </b-form-group>

      <b-button type="submit" variant="primary">Submit</b-button>
      &nbsp;
      <b-button type="reset" variant="">Reset</b-button>

    </b-form>
  </div>
</template>

<script>
import { RepositoryFactory } from './../repositories/RepositoryFactory'
const RDBMSRepository = RepositoryFactory.get('rdbmsRepository')

export default {
  name: "ActorForm",
  components: {
  },
  data() {
    return {
      id: this.$route.params.id,
      actor : {
        first_name : null,
        last_name : null,
        dob : null,
      },
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
      const { data } = await RDBMSRepository.findActorById( this.$route.params.id)
      this.isLoading = false
      this.actor = data;
    },
    async onSubmit(evt) {
      evt.preventDefault()
      await RDBMSRepository.updateActor( this.actor)
      this.msg = "Actor Updated";
    },
    onReset(evt) {
      evt.preventDefault()
    }
  }
};
</script>

<style scoped>
#formContainer {
  padding-right: 100px;
  padding-left: 100px;

}
</style>