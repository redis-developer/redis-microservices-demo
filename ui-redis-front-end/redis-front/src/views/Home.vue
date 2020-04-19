<template>
  <div class="home">
    <b-jumbotron>
      <h1>Redis Movie Database</h1>
      <h2>
      Modernize legacy application with <b>Redis</b> and <b>Microservices</b>.
      </h2>
    </b-jumbotron>



<div>
  <b-card no-body>
    <b-tabs card>
      <b-tab title="Overall Architecture" active>
        <b-card-text>
              <img alt="Architecture" src="/imgs/overal-archi.png">
        </b-card-text>
      </b-tab>
      <b-tab title="Notifications">
              <img alt="Gears to Pub Sub" src="/imgs/gears-pubsub.png">
      </b-tab>
    </b-tabs>
  </b-card>
</div>


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
