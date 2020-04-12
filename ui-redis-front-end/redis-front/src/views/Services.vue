<template>
  <div id="app" class="px-8 py-8 min-h-screen">
    <ServicesParent/>
  </div>
</template>

<script>
import ServicesParent from "../components/ServicesParent";

export default {
  name: "App",
  components: {
    ServicesParent
  },
  created() {
        this.$parent.contextualHelp = "Use this page to start/stop the Streaming services, that read or write to Redis Streams";
  }
};
</script>


