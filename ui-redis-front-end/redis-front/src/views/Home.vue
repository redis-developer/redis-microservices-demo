<template>
  <div class="home">
    <b-jumbotron>
      <h2>
      Modernize legazy application with <b>Redis</b> and <b>Microservices</b>.
      </h2>
    </b-jumbotron>

    <img alt="Architecture" src="/imgs/overal-archi.png">

    <b-container>

    </b-container>


  </div>
</template>

<script>

export default {
  name: 'Home',
    created() {
    this.$parent.contextualHelp = null;
  },
}
</script>
