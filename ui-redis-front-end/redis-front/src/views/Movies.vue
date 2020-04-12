<template>
  <div id="app" class="px-8 py-8 min-h-screen">

    <h1>Movies <small>(from MySQL)</small></h1>
    
    <MainComponent/>
  </div>
</template>

<script>
import MainComponent from "../components/MovieList";

export default {
  name: "MovieList",
  components: {
    MainComponent
  }
};
</script>