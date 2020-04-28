<template>
  <div id="app" class="px-8 py-8 min-h-screen">
    <ServicesParent/>
    <div class="mt-4">
    <hr/>
    <ServicesConfiguration/>
    </div>

  </div>
</template>

<script>
import ServicesParent from "../components/ServicesParent";
import ServicesConfiguration from "../components/ServicesConfiguration";

export default {
  name: "App",
  components: {
    ServicesParent,
    ServicesConfiguration
  },
  created() {
        this.$parent.contextualHelp = "Use this page to start/stop the Streaming services, that read or write to Redis Streams";
  }
};
</script>


