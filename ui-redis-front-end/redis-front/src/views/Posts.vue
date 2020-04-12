<template>
  <div id="app" class="px-8 py-8 min-h-screen">
    <PostsParent/>
  </div>
</template>

<script>
import PostsParent from "../components/PostsParent";

export default {
  name: "App",
  components: {
    PostsParent
  }
};
</script>


