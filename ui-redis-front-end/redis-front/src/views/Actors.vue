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
  }
};
</script>