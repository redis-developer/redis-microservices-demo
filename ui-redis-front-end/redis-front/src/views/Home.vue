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
              
              <div class="text-left">
                <ul>
                  <li>Improve data access speed, reduce cost with advanced <b>caching</b></li>
                  <li>Prepare a Modern integration with a <b>streaming based architecture</b></li>
                  <li>Publish RDBMS & Business Events to <b>Redis Streams</b></li>
                  <li>Add new services to your legacy application, for example full text search, and rich analytics with <b>RediSearch</b></li>
                  <li>Consume the same events to manage a cache, a new data model for example a <b>Graph Database</b></li>
                  <li>Create lighweight notification service with <b>Redis Pub/Sub</b> and Web Sockets</li>
                  <li><i>Move new business logic in new services and keep legacy ain sync with <b>Redis Gears</b> and its Write Behind service</i></li>
                </ul>
              </div>
        </b-card-text>
      </b-tab>
      <b-tab title="Web Service Caching">
              <img alt="Ws Caching" src="/imgs/ws-caching.png">
              <div class="text-left">
                <br/>
                <ul>
                  <li>Cache the result of expensive calls into <b>Redis</b> datastructure</li>
                  <li>In this demo with <b>dozen of lines</b> of code we move from 200ms to 0-3ms for each call</li>
                  <li>Chose the <b>datastructure</b> that fits your needs.</li>
                  <li>Associate a <b>Time to Live</b> to the value your put in the cache and do not think about it!.</li>
                </ul>
              </div>
      </b-tab>
      <b-tab title="Notifications">
              <img alt="Gears to Pub Sub" src="/imgs/gears-pubsub.png">
                            <div class="text-left">
                <br/>
                <ul>
                  <li>If you need fast and easy communication betwee service do not forget about <b>Redis Pub/Sub</b> !</li>
                  <li>Associated with <b>Redis Gears</b> you can create Serverless and Reactive Application in minutes!</li>
                  <li>In this part of the application, with 3 lines of codes you take any "RDBMS event" out of the Streams and publish it as an notification "fire & forget"</li>
                  <li>Then using a Web Socket server you can broadcast the event to any service and client that need it</li>

                </ul>
              </div>
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
