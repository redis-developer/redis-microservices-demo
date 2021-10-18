<template>
  <div id="app" class="px-8 py-8 min-h-screen">

    <h1>Actors <small>(from MySQL)</small></h1>
    
    <ActorMainComponent/>
  </div>
</template>

<script>
import ActorMainComponent from "../components/ActorList";

export default {
  name: "App",
  components: {
    ActorMainComponent
  },
  created() {
        this.$parent.contextualHelp = "Legacy Application calling a REST API providing SQL data, see "+
        "<b><a target='_blank' href='https://github.com/redis-developer/redis-microservices-demo/blob/master/sql-data-generator/src/main/java/io/redis/demos/debezium/sql/controllers/ActorsAPIController.java'>ActorsAPIController</a></b>";
  }
};
</script>