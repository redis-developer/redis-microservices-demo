<template>
  <div id="app">

<div style="margin-bottom:10px">
  <b-navbar toggleable="lg" type="dark" variant="dark">
    <b-navbar-brand style="margin-left:90px" to="/" > RMDb</b-navbar-brand>
    <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>
    <b-collapse id="nav-collapse" is-nav>
      <b-navbar-nav>
        <b-nav-item  to="/movies" > | <b-icon-camera-video /> Movies <small>(Legacy)</small> </b-nav-item>
        <b-nav-item  to="/actors" > <b-icon-people-circle /> Actors <small>(Legacy)</small> | </b-nav-item>
        <b-nav-item>  </b-nav-item>
        <b-nav-item>  </b-nav-item>
        <b-nav-item>  </b-nav-item>
        <b-nav-item>  </b-nav-item>
        <b-nav-item><img src="/imgs/redis-logo.svg" height="30" /></b-nav-item>  
        <b-nav-item-dropdown text="Search " right>     <!-- Using 'button-content' slot -->
          <b-dropdown-item to="/search/movies" > <b-icon-camera-video /> Movies</b-dropdown-item>
          <b-dropdown-item to="/search/actors" > <b-icon-people-circle />  Actors</b-dropdown-item>
          <b-dropdown-item to="/autocomplete" >Autocomplete</b-dropdown-item>
        </b-nav-item-dropdown>
        <b-nav-item  to="/stats" >  <b-icon-graph-up /> Statistics</b-nav-item>
        <b-nav-item>|  </b-nav-item>
        <b-nav-item  to="/services" > <b-icon-cone-striped /> Services  </b-nav-item>
      </b-navbar-nav>
      

      <b-navbar-nav class="ml-auto">
        <img src="/imgs/redislabs.png" height="30" />
      </b-navbar-nav>
      
    </b-collapse>
  </b-navbar>

</div>



<router-view />




<b-alert id="notification" 
  v-if="notif"   
  variant="info" 
  :max="dismissSecs"
  :value="dismissCountDown"
  dismissible 
  :show="dismissCountDown"
  @dismissed="deleteNotification" >
  Movie <b-link :to="{ name: 'MovieForm', params: { id: notif.id } }">"{{notif.title}}"</b-link> updated...
</b-alert> 

<div id="sticky" v-if="contextualHelp" >
  <b-button id="popover-target-1">
    <svg class="bi bi-eye" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
      <path fill-rule="evenodd" d="M16 8s-3-5.5-8-5.5S0 8 0 8s3 5.5 8 5.5S16 8 16 8zM1.173 8a13.134 13.134 0 001.66 2.043C4.12 11.332 5.88 12.5 8 12.5c2.12 0 3.879-1.168 5.168-2.457A13.134 13.134 0 0014.828 8a13.133 13.133 0 00-1.66-2.043C11.879 4.668 10.119 3.5 8 3.5c-2.12 0-3.879 1.168-5.168 2.457A13.133 13.133 0 001.172 8z" clip-rule="evenodd"/>
      <path fill-rule="evenodd" d="M8 5.5a2.5 2.5 0 100 5 2.5 2.5 0 000-5zM4.5 8a3.5 3.5 0 117 0 3.5 3.5 0 01-7 0z" clip-rule="evenodd"/>
    </svg>
  </b-button>
  <b-popover target="popover-target-1" triggers="hover" placement="top">
    <template v-slot:title>Project Information</template>
    <span v-html="contextualHelp"></span>
  </b-popover>
</div>

<a href="https://github.com/tgrall/redis-microservices-demo"><img src="/imgs/forkme_left_red.svg" style="position:absolute;top:0;left:0;" alt="Fork me on GitHub"></a>

  </div>
</template>

<script>
let ws = null;

    export default {
        name: 'App',
        data() {
            return {
              dismissSecs: 5,
              dismissCountDown: 0,
              showDismissibleAlert: false,
              notif : "",
              contextualHelp: null,
            }
        },
        created() {

          try {
            ///ws =new WebSocket(`ws://${ location.host}/notifications`);
            ws =new WebSocket(`ws://localhost:8888/`);
            ws.onmessage = ({ data }) => {
              console.log(data)
              const event = JSON.parse(data);
              this.notif = event;
              this.dismissCountDown = this.dismissSecs
            };
          }
          catch(err){
            console.log(err);
          }


        },
        methods : {
          deleteNotification () {
            this.notif = null;
          },        
          countDownChanged(dismissCountDown) {
            this.dismissCountDown = dismissCountDown
          },
        }
    }
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  padding-left: 20px;
  padding-right: 20px;
}

#nav {
  padding: 30px;
}

#nav a {
  font-weight: bold;
  color: #2c3e50;
}

#nav a.router-link-exact-active {
  color: #42b983;
}

#sticky {
  position: fixed; 
  bottom: 5px; 
  right: 10px;
  z-index: 99;
 }


#notification {
  position: fixed; 
  bottom: 5px; 
  left: 10px;
  z-index: 99;
 }

</style>
