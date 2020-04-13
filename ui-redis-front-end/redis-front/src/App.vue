<template>
  <div id="app">


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

    <div id="nav">
      <router-link to="/">Home</router-link> |
      <router-link to="/actors">Actors</router-link>  |
      <router-link to="/movies">Movies</router-link>  |
      <router-link to="/autocomplete">Autocomplete</router-link> |
      <router-link to="/search">Search</router-link> |
      <router-link to="/aggregations">Aggregations</router-link> |
      <router-link to="/services">Services</router-link>
    </div>
    

    <router-view/>
  </div>
</template>

<script>
    export default {
        name: 'App',
        data() {
            return {
                contextualHelp: null,
            }
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

#sticky{
  position: fixed;
  right: 16px;
  z-index: 99;
  margin: 5px;
}


</style>
