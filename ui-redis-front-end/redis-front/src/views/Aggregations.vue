<template>
  <div id="app" class="px-8 py-8 min-h-screen">

    <h1>Aggregations with RediSearch</h1>
    
        <b-form @submit="changeSelected" >

    <b-button variant='primary' >View</b-button>
        </b-form>

  </div>
</template>

<script>
export default {
 data () {
        return {
        }
      },
      methods: {
        changeSelected (evt) {
          console.log("TEST")
          this.selected = true
        }
      }
 
};
</script>